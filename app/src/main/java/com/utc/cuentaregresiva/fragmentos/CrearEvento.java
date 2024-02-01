package com.utc.cuentaregresiva.fragmentos;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.utc.cuentaregresiva.R;
import com.utc.cuentaregresiva.entidades.BaseDatos;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class CrearEvento extends Fragment {


    private TextView txt_id_evento;
    private EditText edt_titulo_evt, edt_descripcion_evt, edt_f_final, edt_hora_final;
    private ImageView img_evt;

    /* 3 Variables para el manejo de la seleccion de imagen */
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageFilePath;
    private Bitmap imageToStore;

    private Button btn_fecha, btn_hora, btn_registrar;

    int dia, mes, year;

    SharedPreferences preferencias;
    SharedPreferences.Editor editor;

    private BaseDatos bdd;

    // Conseguir la fecha del sistema
    private Date fecha = new Date();
    // Dar formato a la fecha
    CharSequence s1 = DateFormat.format("yyyy-MM-dd", fecha.getTime());
    String fechas_auxiliar[]; //  Array para almacenar las partes de la fecha actual
    // Array para almacenar las partes de la fecha actual transformadas a entero
    int fechas[] = new int[3];


    public CrearEvento() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bdd = new BaseDatos(getContext());


    }

    // Aqui se pone la logica de los elementos del fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        preferencias = requireActivity().getSharedPreferences("inicio_sesion", Context.MODE_PRIVATE);

        // Se usa la vista para enlazar los elementos logicos con los elementos graficos
        View vista = inflater.inflate(R.layout.fragment_crear_evento, container, false);

        // Elemento = vista.findViewById(R.id.elementoxml);
        // Mapear o relacionar los componentes logicos con los componentes graficos
        txt_id_evento = vista.findViewById(R.id.txt_id_evento);
        edt_titulo_evt = vista.findViewById(R.id.edt_titulo_evt);
        edt_descripcion_evt = vista.findViewById(R.id.edt_descripcion_evt);
        edt_f_final = vista.findViewById(R.id.edt_f_final);
        edt_hora_final = vista.findViewById(R.id.edt_hora_final);
        img_evt = vista.findViewById(R.id.img_evt);

        btn_fecha = vista.findViewById(R.id.btn_fecha);
        btn_hora = vista.findViewById(R.id.btn_hora);

        btn_registrar = vista.findViewById(R.id.btn_registrar);

        // Deshabilitar el EditText de la fecha y hora
        edt_f_final.setKeyListener(null);
        edt_hora_final.setKeyListener(null);

        // Separar el dia, mes y anio de la fecha
        fechas_auxiliar = String.valueOf(s1).split("-");
        // Transformar el dia, mes y anio en entero
        for (int i = 0; i < fechas_auxiliar.length; i++) {
            fechas[i] = Integer.parseInt(fechas_auxiliar[i]);
        }
        int fk_usuario = preferencias.getInt("id_usuario", 0);
        // Colocar el id del proximo registro en el un TextView
        txt_id_evento.setText("ID Evento: " + bdd.conseguirCountEventos(fk_usuario));

        btn_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarFecha();
            }
        });


        btn_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarHora();
            }
        });

        img_evt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarImagen();
            }
        });

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarEvento();
            }
        });

        // CONTEXTO DEL FRAGMENT ===> getContext() en lugar de this
        return vista;
    }

    // Proceso 1: Metodo para registrar un evento
    public void registrarEvento() {
        // Obtener el contenido del formulario
        String titulo = edt_titulo_evt.getText().toString();
        String descripcion = edt_descripcion_evt.getText().toString();
        String f_final = edt_f_final.getText().toString();
        String hora_final = edt_hora_final.getText().toString();
        int fk_usuario = preferencias.getInt("id_usuario", 0);
        Bitmap imagen = null;
        if (imageToStore != null) {
            imagen = imageToStore;
        }

        // Condicion para validar que todos los campos esten llenos
        if (validarEspaciosBlanco(titulo, descripcion, f_final, hora_final)) {
            bdd.registrarEvento(titulo, descripcion, f_final, hora_final, fk_usuario, imagen);
            limpiarCampos();
            Toast.makeText(getContext(), "Nuevo Evento Registrado Correctamente!", Toast.LENGTH_SHORT).show();
            txt_id_evento.setText("ID Evento: " + bdd.conseguirCountEventos(fk_usuario));


        } else {
            Toast.makeText(getContext(), "Debe llenar todos los campos!", Toast.LENGTH_SHORT).show();
        }
    }


    // Proceso 2: Metodo para validar que no haya campos vacios
    public Boolean validarEspaciosBlanco(String titulo, String descripcion, String f_final, String hora_final) {
        boolean valido = true;
        if (titulo.isEmpty()) {
            edt_titulo_evt.setError("Debe Ingresar un Titulo del Evento");
            valido = false;
        }
        if (descripcion.isEmpty()) {
            edt_descripcion_evt.setError("Debe Ingresra una Descripcion del Evento");
            valido = false;
        }
        if (f_final.isEmpty()) {
            edt_f_final.setError("Debe Seleccionar una Fecha Limite");
            valido = false;
        }
        if (hora_final.isEmpty()) {
            edt_hora_final.setError("Debe Seleccionar una Hora Limite");
            valido = false;
        }
        return valido;
    }

    // Proceso 3: Seleccionar una Fecha con DatePickerDialog
    private void seleccionarFecha() {
        final LocalDate fechaActual = LocalDate.now();
        final Calendar c = Calendar.getInstance();
        // Estas tres variables son para colocar la fecha por defecto
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);

        // El estilo esta en res/style(21) -> DialogTheme
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                LocalDate fechaSeleccionada = LocalDate.of(y, m + 1, d);

                if (fechaSeleccionada.isEqual(fechaActual) || fechaSeleccionada.isAfter(fechaActual)) {
                    edt_f_final.setError(null);
                    String mesFormat = String.format("%02d", m + 1);
                    String diaFormat = String.format("%02d", d);
                    edt_f_final.setText(y + "-" + mesFormat + "-" + diaFormat);
                    edt_hora_final.setText("");
                } else {
                    edt_f_final.setError("La fecha debe ser igual o mayor a la fecha actual");
                    edt_f_final.setText("");
                    Toast.makeText(getContext(), "La Fecha debe ser igual o mayor que la fecha actual!", Toast.LENGTH_SHORT).show();
                }
            }
        }, year, mes, dia);
        datePickerDialog.show();
    }

    // Proceso 4: Seleccionar una Hora con TimePickerDialog
    private void seleccionarHora() {
        String fechaLimite = edt_f_final.getText().toString();
        System.out.println("FECHA SELECCIONADA: " + fechaLimite);
        if (!fechaLimite.isEmpty()) {
            final LocalDate fechaSeleccionada = LocalDate.parse(fechaLimite);

            final Calendar calendar = Calendar.getInstance();
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            int currentMinute = calendar.get(Calendar.MINUTE);

            // Este estilo esta en res/stsyle(21) -> DialogTheme
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                    LocalDate fechaActual = LocalDate.now();
                    LocalTime horaSeleccionada = LocalTime.of(hours, minutes);
                    LocalTime horaActual = LocalTime.now();
                    if (fechaSeleccionada.isEqual(fechaActual)) {
                        if (horaSeleccionada.isAfter(horaActual)) {
                            edt_hora_final.setError(null);
                            String formatoCorregido = formatTime(hours, minutes);
                            edt_hora_final.setText(formatoCorregido);
                        } else {
                            edt_hora_final.setError("La tiempo limite tiene que ser mayor al tiempo actual");
                            Toast.makeText(getContext(), "El tiempo limite tiene que ser mayor al tiempo actual", Toast.LENGTH_SHORT).show();
                            edt_hora_final.setText("");
                        }
                    } else {
                        edt_hora_final.setError(null);
                        String formatoCorregido = formatTime(hours, minutes);
                        edt_hora_final.setText(formatoCorregido);
                    }
                }
            }, currentHour, currentMinute, true);
            timePickerDialog.show();
        } else {
            edt_hora_final.setError("Seleccione una Fecha");
            Toast.makeText(getContext(), "Primero debe seleccionar una Fecha Limite", Toast.LENGTH_SHORT).show();
        }
    }

    // Proceso 5: Dar formato a la hora seleccionada HH:mm
    private String formatTime(int hourOfDay, int minute) {
        // Create a SimpleDateFormat to format the time
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // Create a Calendar instance and set the selected hour and minute
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        // Format the time and return the result
        return sdf.format(calendar.getTime());
    }

    // Proceso 6: Metodo para limpiear todos los campos
    private void limpiarCampos() {
        edt_titulo_evt.setText("");
        edt_descripcion_evt.setText("");
        edt_f_final.setText("");
        edt_hora_final.setText("");
    }

    // Proceso 7: Seleccionar una Imagen
    private void seleccionarImagen() {
        try {
            Intent objectIntent = new Intent();
            objectIntent.setType("image/*");

            objectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(objectIntent, PICK_IMAGE_REQUEST);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Proceso 8: Recibir la Imagen Seleccionada por el Usuario
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageFilePath = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageFilePath);
                int ancho = img_evt.getWidth();
                int altura = img_evt.getHeight();
                Bitmap nuevaImagen = Bitmap.createScaledBitmap(imageToStore, ancho, altura, true);
                img_evt.setImageBitmap(nuevaImagen);
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();;
        }
    }

}
