package com.utc.cuentaregresiva;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.utc.cuentaregresiva.fragmentos.CrearEvento;
import com.utc.cuentaregresiva.fragmentos.EditarEvento;
import com.utc.cuentaregresiva.fragmentos.ListaEventos;
import com.utc.cuentaregresiva.fragmentos.OnBackPressedListener;
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
        AndroidThreeTen.init(this); // Permite Utilizar las funciones del Cronometro en los fragmentos

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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Clear the menu items
        MenuInflater customMenu = getMenuInflater();
        customMenu.inflate(R.menu.bottom_nav_menu, menu);

        return super.onPrepareOptionsMenu(menu);
    }

    private void bottomMenu() {
        // Este metodo se puede utilizar con la dependencia de :
        // implementation 'org.jetbrains.kotlin:kotlin-stdlib:1.3.72'
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int pos) {
                String valor = "";
                Fragment fragment = null;
                switch (pos) {
                    case R.id.bottom_nav_dashboard:
                        valor = "lista";
                        fragment = new ListaEventos();
                        break;
                    case R.id.bottom_nav_manage:
                        valor = "crear";
                        fragment= new CrearEvento();
                        break;
                    case R.id.bottom_nav_profile:
                        valor = "perfil";
                        fragment = new PerfilUsuario();
                        break;
                }
                System.out.println("Ingresar: " + valor);
                // Clear the back stack before replacing the fragment

                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                // Colocar el fragmento seleccionado del Menu

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, valor).commit();
            }
        });
    }

    @Override
    protected  void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("estado", "menu_principal");
    }


    @Override
    public void onBackPressed() {
        /* Decidir cual onBackPressed() utilizar, si el de activity o el de algun fragmento */
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof OnBackPressedListener) {
            if (((OnBackPressedListener) currentFragment).onBackPressed()) {
                super.onBackPressed();
                return;
            }
        }

        /* Mover la actividad a segundo plano */
        moveTaskToBack(true);

    }

}
