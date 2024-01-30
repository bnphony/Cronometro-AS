package com.utc.cuentaregresiva.fragmentos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.utc.cuentaregresiva.R;
import com.utc.cuentaregresiva.entidades.BaseDatos;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarEvento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarEvento extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int idEvento, fkUsuario;
    private String titulo, descripcion, fechaLimite, horaLimite;

    private TextView txt_id_evento;
    private EditText edt_titulo_evt, edt_descripcion_evt, edt_f_final, edt_hora_final;
    private Button btn_fecha, btn_hora, btn_editar, btn_eliminar, btn_cancelar;

    private int dia, mes, year;

    private BaseDatos bdd;

    // Conseguir la fecha del sistema
    private Date fecha = new Date();
    // Dar formato a la fecha
    CharSequence s1 = DateFormat.format("yyyy-MM-dd", fecha.getTime());
    String fechas_auxiliar[]; //  Array para almacenar las partes de la fecha actual
    // Array para almacenar las partes de la fecha actual transformadas a entero
    int fechas[] = new int[3];

    public EditarEvento() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditarEvento.
     */
    // TODO: Rename and change types and number of parameters
    public static EditarEvento newInstance(String param1, String param2) {
        EditarEvento fragment = new EditarEvento();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            idEvento = getArguments().getInt("id_evento", 0);
            titulo = getArguments().getString("titulo", "");
            descripcion = getArguments().getString("descripcion", "");
            fechaLimite = getArguments().getString("fecha", "");
            horaLimite = getArguments().getString("hora", "");
            fkUsuario = getArguments().getInt("fk_usuario", 0);
        }
        bdd = new BaseDatos(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_editar_evento, container, false);

        // Elemento = vista.findViewById(R.id.elementoxml);
        // Mapear o relacionar los componentes logicos con los componentes graficos
        txt_id_evento = vista.findViewById(R.id.txt_id_evento);
        edt_titulo_evt = vista.findViewById(R.id.edt_titulo_evt);
        edt_descripcion_evt = vista.findViewById(R.id.edt_descripcion_evt);
        edt_f_final = vista.findViewById(R.id.edt_f_final);
        edt_hora_final = vista.findViewById(R.id.edt_hora_final);

        btn_fecha = vista.findViewById(R.id.btn_fecha);
        btn_hora = vista.findViewById(R.id.btn_hora);

        btn_editar = vista.findViewById(R.id.btn_editar);
        btn_eliminar = vista.findViewById(R.id.btn_eliminar);
        btn_cancelar = vista.findViewById(R.id.btn_cancelar);

        // Deshabilitar el EditText de la fecha y hora
        edt_f_final.setKeyListener(null);
        edt_hora_final.setKeyListener(null);

        // Separar el dia, mes y anio de la fecha
        fechas_auxiliar = String.valueOf(s1).split("-");
        // Transformar el dia, mes y anio en entero
        for (int i = 0; i < fechas_auxiliar.length; i++) {
            fechas[i] = Integer.parseInt(fechas_auxiliar[i]);
        }
        // Colocar el id del proximo registro en el un TextView
        txt_id_evento.setText("ID Evento: " + idEvento);
        edt_titulo_evt.setText(titulo);
        edt_descripcion_evt.setText(descripcion);
        edt_f_final.setText(fechaLimite);
        edt_hora_final.setText(horaLimite);

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

        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editarEvento();
            }
        });

        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarEvento();
            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListaEventos fragment = new ListaEventos();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment, "lista");
                transaction.commit();
            }
        });

        return vista;
    }



    // Proceso 1: Metodo para editar un evento
    private void editarEvento() {
        // Obtener el contenido del formulario
        String nuevoTitulo = edt_titulo_evt.getText().toString();
        String nuevaDescripcion = edt_descripcion_evt.getText().toString();
        String f_final = edt_f_final.getText().toString();
        String hora_final = edt_hora_final.getText().toString();
        String auxiliarFecha[]; // Para comprobar la fecha
        String auxiliarHora[];
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        // Condicion para validar que todos los campos esten llenos
        if (validarEspaciosBlanco(nuevoTitulo, nuevaDescripcion, f_final, hora_final)) {
            edt_f_final.setError(null);
            edt_hora_final.setError(null);
            auxiliarFecha = f_final.split("-");
            // Condicion para comprobar si la fecha es igual o mayor a la fecha actual
            if (Integer.parseInt(auxiliarFecha[0]) >= fechas[0] && Integer.parseInt(auxiliarFecha[1]) >= fechas[1]
                    && Integer.parseInt(auxiliarFecha[2]) >= fechas[2]) {
                edt_f_final.setError(null); // Quitar el mensaje de error
                auxiliarHora = hora_final.split(":");
                if (Integer.parseInt(auxiliarHora[0]) >= currentHour && Integer.parseInt(auxiliarHora[1]) >= currentMinute) {
                    edt_hora_final.setError(null);
                    bdd.actualizarEvento(idEvento, nuevoTitulo, nuevaDescripcion, f_final, hora_final);
                    Toast.makeText(getContext(), "Evento Actualizado Correctamente!", Toast.LENGTH_SHORT).show();
                } else {
                    edt_hora_final.setError("La hora ya no esta disponible!");
                    Toast.makeText(getContext(), "La hora ya no esta disponible", Toast.LENGTH_SHORT).show();
                }

            } else {
                edt_f_final.setError("La fecha debe ser igual o mayor a la fecha actual");
                Toast.makeText(getContext(), "La fecha debe ser igual o mayor a la fecha actual", Toast.LENGTH_SHORT).show();
            }
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
        final Calendar c = Calendar.getInstance();
        // Estas tres variables son para colocar la fecha por defecto
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);

        // El estilo esta en res/style(21) -> DialogTheme
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                edt_f_final.setText(y + "-" + (m + 1) + "-" + d);
            }
        }, year, mes, dia);
        datePickerDialog.show();
    }

    // Proceso 4: Seleccionar una Hora con TimePickerDialog
    private void seleccionarHora() {
        final Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        // Este estilo esta en res/stsyle(21) -> DialogTheme
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                String formatoCorregido = formatTime(hours, minutes);
                edt_hora_final.setText(formatoCorregido);
            }
        }, currentHour, currentMinute, true);
        timePickerDialog.show();
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

    // Proceso 7: Metodo para eliminar un evento
    private void eliminarEvento() {
        AlertDialog.Builder estructuraConfirmacion = new AlertDialog.Builder(getContext())
                .setTitle("Confirmacion")
                .setMessage("Esta seguro que quiere eliminar este evento?")
                .setPositiveButton("SI, Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bdd.eliminarEvento(idEvento);
                        Toast.makeText(getContext(), "Se elimino el Evento Correctamente!", Toast.LENGTH_SHORT).show();
                        ListaEventos fragment = new ListaEventos();
                        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.commit();
                    }
                })
                .setNegativeButton("NO, Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Eliminacion Cancelada!", Toast.LENGTH_SHORT).show();
                    }
                }).setCancelable(true);
        AlertDialog cuadroDialogo = estructuraConfirmacion.create();
        cuadroDialogo.show();
    }

}
