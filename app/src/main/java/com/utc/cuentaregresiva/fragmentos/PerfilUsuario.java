package com.utc.cuentaregresiva.fragmentos;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.utc.cuentaregresiva.IniciarSesion;
import com.utc.cuentaregresiva.R;
import com.utc.cuentaregresiva.entidades.BaseDatos;

public class PerfilUsuario extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText edt_nombre_usu, edt_nombre_usuario_usu, edt_email_usu, edt_password;
    private Button btn_cambiar_password, btn_guardar, btn_cerrar_sesion;

    SharedPreferences preferencias;
    SharedPreferences.Editor editor;
    private int idUsuario = 0;
    private String nombre, nombre_usuario, email, password, confirm_password;

    private BaseDatos bdd;

    public PerfilUsuario() {
        // Required empty public constructor
    }

    public static PerfilUsuario newInstance(String param1, String param2) {
        PerfilUsuario fragment = new PerfilUsuario();
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

        }
        preferencias = requireActivity().getSharedPreferences("inicio_sesion", Context.MODE_PRIVATE);
        idUsuario = preferencias.getInt("id_usuario", 0);
        editor = preferencias.edit();

        bdd = new BaseDatos(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_perfil_usuario, container, false);

        // Mapear los componentes logicos con los componentes graficos
        edt_nombre_usu = vista.findViewById(R.id.edt_nombre_usu);
        edt_nombre_usuario_usu = vista.findViewById(R.id.edt_nombre_usuario_usu);
        edt_email_usu = vista.findViewById(R.id.edt_email_usu);
        edt_password = vista.findViewById(R.id.edt_password);
        edt_password.setKeyListener(null);

        btn_cambiar_password = vista.findViewById(R.id.btn_cambiar_password);
        btn_guardar = vista.findViewById(R.id.btn_guardar);
        btn_cerrar_sesion = vista.findViewById(R.id.btn_cerrar_sesion);

        btn_cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesion();
            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarPerfil();
            }
        });

        consultarUsuario();

        btn_cambiar_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialogo = new Dialog(getContext());
                dialogo.setTitle("Actualizar Password");
                dialogo.setCancelable(true);
                dialogo.setContentView(R.layout.dialogo_password);
                dialogo.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialogo.show();

                final EditText diag_password = dialogo.findViewById(R.id.edt_password);
                final EditText diag_confirm_password = dialogo.findViewById(R.id.edt_confirm_password);
                Button btn_cambiar_password = dialogo.findViewById(R.id.btn_guardar_password);
                Button btn_cancelar_password = dialogo.findViewById(R.id.btn_cancelar_password);

                btn_cambiar_password.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String password = diag_password.getText().toString();
                        String confirmacion_password = diag_confirm_password.getText().toString();
                        if (!password.isEmpty() && !confirmacion_password.isEmpty()) {
                            if (password.equals(confirmacion_password)) {
                                bdd.actualizarPassword(idUsuario, password);
                                edt_password.setText(password);
                                dialogo.dismiss();
                                Toast.makeText(getContext(), "Password Actualizado Correctamente!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Los Passwords no coinciden!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Debe llenar el campo Password y la Confirmacion", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                btn_cancelar_password.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogo.dismiss();
                    }
                });



            }
        });

        return vista;
    }

    // Proceso 1: Consultar la Informacion del Usuario
    private void consultarUsuario() {
        Cursor usuarioEncontrado = bdd.obtenerUsuario(idUsuario);
        if (usuarioEncontrado != null) {
            nombre = usuarioEncontrado.getString(1);
            nombre_usuario = usuarioEncontrado.getString(2);
            email = usuarioEncontrado.getString(3);
            password = usuarioEncontrado.getString(4);

            edt_nombre_usu.setText(nombre);
            edt_nombre_usuario_usu.setText(nombre_usuario);
            edt_email_usu.setText(email);
            edt_password.setText(password);

        } else {
            Toast.makeText(getContext(), "Error! No se encontro un Usuario", Toast.LENGTH_SHORT).show();
        }
    }

    // Proceso 2: Actualizar la informacion del Usuario
    private void actualizarPerfil() {
        String nNombre = edt_nombre_usu.getText().toString();
        String nNombreUsuario = edt_nombre_usuario_usu.getText().toString();
        String nEmail = edt_email_usu.getText().toString();

        if (!nNombre.isEmpty() && !nNombreUsuario.isEmpty() && !nEmail.isEmpty()) {
            if (!bdd.validarNombreUsuarioExistente(idUsuario, nNombreUsuario)) {
                bdd.actualizarPerfil(idUsuario, nNombre, nNombreUsuario, nEmail);
                Toast.makeText(getContext(), "Perfil Actualizado Correctamente!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error! El Nombre de Usuario no esta disponible!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    // Proceso 3: Cerrar Sesion
    private void cerrarSesion() {
        requireActivity().finish();
        editor.putBoolean("sesion", false);
        editor.putString("nombre_usuario", "");
        editor.putString("password", "");
        editor.apply();
        Toast.makeText(getContext(), "Sesion Finalizada", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), IniciarSesion.class);
        startActivity(intent);
    }

}
