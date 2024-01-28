package com.utc.cuentaregresiva;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.utc.cuentaregresiva.fragmentos.CrearEvento;
import com.utc.cuentaregresiva.fragmentos.ListaEventos;
import com.utc.cuentaregresiva.fragmentos.PerfilUsuario;

public class MenuPrincipal extends AppCompatActivity {
    TextView txt_usuario;

    SharedPreferences preferencias;
    SharedPreferences.Editor editor;

    ChipNavigationBar chipNavigationBar; // Barra de navegacion de abajo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        // Relacionar los elementos logicos con los elementos graficos
        chipNavigationBar = findViewById(R.id.bottom_nav_menu);
        // Marcar por defecto la opcion ListaEventos en el Menu
        chipNavigationBar.setItemSelected(R.id.bottom_nav_dashboard, true);
        // Se coloca como valor por defecto el Fragmento ListaEventos()
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListaEventos()).commit();
        bottomMenu();


        txt_usuario = (TextView) findViewById(R.id.txt_usuario);

        preferencias = getSharedPreferences("inicio_sesion", Context.MODE_PRIVATE);
        txt_usuario.setText("Bienvenido < "+ preferencias.getString("nombre_usuario", "") + " >");
        editor = preferencias.edit();

    }

    private void bottomMenu() {
        // Este metodo se puede utilizar con la dependencia de :
        // implementation 'org.jetbrains.kotlin:kotlin-stdlib:1.3.72'
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int pos) {
                Fragment fragment = null;
                switch (pos) {
                    case R.id.bottom_nav_dashboard:
                        fragment = new ListaEventos();
                        break;
                    case R.id.bottom_nav_manage:
                        fragment= new CrearEvento();
                        break;
                    case R.id.bottom_nav_profile:
                        fragment = new PerfilUsuario();
                        break;
                }
                // Colocar el fragmento seleccionado del Menu
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });
    }

    public void cerrarSesion(View view) {
        finish();
        editor.putBoolean("sesion", false);
        editor.putString("nombre_usuario", "");
        editor.putString("password", "");
        editor.apply();
        Toast.makeText(getApplicationContext(), "Sesion Finalizada", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), IniciarSesion.class);
        startActivity(intent);
    }
}
