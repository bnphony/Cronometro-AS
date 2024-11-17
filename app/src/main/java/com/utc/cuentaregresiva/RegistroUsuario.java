package com.utc.cuentaregresiva;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.utc.cuentaregresiva.entidades.BaseDatos;

public class RegistroUsuario extends AppCompatActivity {

    EditText edt_nombre, edt_nombre_usuario, edt_email, edt_password, edt_confirmacion;
    BaseDatos miBdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        edt_nombre = (EditText) findViewById(R.id.edt_nombre);
        edt_nombre_usuario = (EditText) findViewById(R.id.edt_nombre_usuario);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_password = (EditText) findViewById(R.id.edt_password);
        edt_confirmacion = (EditText) findViewById(R.id.edt_confirmacion);

        miBdd = new BaseDatos(getApplicationContext());
    }

    public void registrarUsuario(View view) {
        String nombre = edt_nombre.getText().toString();
        String nombre_usuario = edt_nombre_usuario.getText().toString();
        String email = edt_email.getText().toString();
        String password = edt_password.getText().toString();
        String confirmacion = edt_confirmacion.getText().toString();

        if (!nombre.isEmpty() && !nombre_usuario.isEmpty() && !email.isEmpty()
            && !password.isEmpty() && !confirmacion.isEmpty()) {
            if (password.equals(confirmacion)) {
                if (!miBdd.validarNombreUsuario(nombre_usuario)) {
                    miBdd.agregarUsuario(nombre, nombre_usuario, email, password);
                    Toast.makeText(getApplicationContext(), "Usuario Registrado Correctamente!", Toast.LENGTH_SHORT).show();
                    limpiar();
                } else {
                    edt_nombre_usuario.setError("Ya existe un registro con este nombre de usuario");
                }
            } else {
                edt_confirmacion.setError("Los passwords no coinciden");
                Toast.makeText(getApplicationContext(), "Los passwords no coinciden", Toast.LENGTH_SHORT).show();
            }
        } else {
            edt_nombre_usuario.setError("Debe llenar el nombre de Usuario");
            edt_nombre.setError("Debe colocar su nombre");
            edt_email.setError("Debe colocar un correo electronico");
            edt_password.setError("Debe colocar un password");
            edt_confirmacion.setError("Debe escribir el password nuevamente");
            Toast.makeText(getApplicationContext(), "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    public void limpiar() {
        edt_nombre.setText("");
        edt_nombre_usuario.setText("");
        edt_email.setText("");
        edt_password.setText("");
        edt_confirmacion.setText("");
    }

    public void volver(View view) {
        onBackPressed();
    }
}
