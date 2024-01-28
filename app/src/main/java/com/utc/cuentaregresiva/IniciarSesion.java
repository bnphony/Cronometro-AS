package com.utc.cuentaregresiva;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.utc.cuentaregresiva.entidades.BaseDatos;

public class IniciarSesion extends AppCompatActivity {

    Button btn_crear_cuenta, btn_iniciar_sesion;
    ImageView imagen;
    TextView logo_texto, logo_titulo;
    TextInputLayout nombre_usuario, password;
    EditText edt_nombre_usuario, edt_password;

    CheckBox check_sesion;
    SharedPreferences preferencias;
    SharedPreferences.Editor editor;
    String llave = "sesion";

    BaseDatos miBdd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btn_crear_cuenta = (Button) findViewById(R.id.btn_crear_cuenta);
        imagen = (ImageView) findViewById(R.id.img_logo);
        logo_texto = (TextView) findViewById(R.id.logo_nombre);
        logo_titulo = (TextView) findViewById(R.id.titulo);
        nombre_usuario = (TextInputLayout) findViewById(R.id.txt_nombre_usuario);
        password = (TextInputLayout) findViewById(R.id.txt_password);
        btn_iniciar_sesion = (Button) findViewById(R.id.btn_iniciar);

        preferencias = this.getSharedPreferences("inicio_sesion", Context.MODE_PRIVATE);
        editor = preferencias.edit();
        check_sesion = (CheckBox) findViewById(R.id.c_sesion);
        edt_nombre_usuario = (EditText) findViewById(R.id.edt_nombre_usuario);
        edt_password = (EditText) findViewById(R.id.edt_password);

        miBdd = new BaseDatos(getApplicationContext());

        if (comprobarSesion()) {
            finish();
            Intent intent = new Intent(getApplicationContext(), MenuPrincipal.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Iniciar Sesion", Toast.LENGTH_SHORT).show();
        }

        btn_crear_cuenta.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistroUsuario.class);
                // Efecto de desplazamiento de los elementos
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(imagen, "logo_imagen");
                pairs[1] = new Pair<View, String>(logo_texto, "logo_texto");
                pairs[2] = new Pair<View, String>(logo_titulo, "logo_titulo");
                pairs[3] = new Pair<View, String>(nombre_usuario, "tran_nombre");
                pairs[4] = new Pair<View, String>(password, "tran_password");
                pairs[5] = new Pair<View, String>(btn_iniciar_sesion, "tran_btn_iniciar");
                pairs[6] = new Pair<View, String>(btn_crear_cuenta, "tran_btn_sesion");

                ActivityOptions opciones = ActivityOptions.makeSceneTransitionAnimation(IniciarSesion.this, pairs);
                startActivity(intent, opciones.toBundle());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void iniciarSesion(View view) {
        String nombre_usuario = edt_nombre_usuario.getText().toString();
        String password = edt_password.getText().toString();

        Cursor usuarioEncontrado = miBdd.validarUsuario(nombre_usuario, password);
        if (!nombre_usuario.isEmpty() && !password.isEmpty()) {
            if (usuarioEncontrado != null) {
                String nombre = usuarioEncontrado.getString(1);
                Toast.makeText(getApplicationContext(), "Bienvenido " + nombre, Toast.LENGTH_SHORT).show();
                finish();
                guardarSesion(check_sesion.isChecked(), nombre_usuario, password);

                Intent intent = new Intent(getApplicationContext(), MenuPrincipal.class);
                startActivity(intent);

            } else {
                Toast.makeText(getApplicationContext(), "Nombre de Usuario o Password Incorrecto", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Debe colocar un Nombre de Usuario y Password", Toast.LENGTH_SHORT).show();
        }
    }

    public void guardarSesion(boolean inicio, String nombre_usuario, String password) {
        editor.putBoolean(llave, inicio);
        editor.putString("nombre_usuario", nombre_usuario);
        editor.putString("password", password);
        editor.apply();
    }

    public boolean comprobarSesion() {
        return this.preferencias.getBoolean(llave, false);
    }
}
