package com.utc.cuentaregresiva.fragmentos;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.utc.cuentaregresiva.R;
import com.utc.cuentaregresiva.entidades.BaseDatos;
import com.utc.cuentaregresiva.entidades.Evento;
import com.utc.cuentaregresiva.entidades.EventosAdapter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class ListaEventos extends Fragment {

    private TextView txt_num_pagina;
    private Button btn_anterior, btn_siguiente;

    private int pagina_actual = 1;
    private int total_paginas = 1;
    private int cantidad_mostrar = 10;


    private List<Evento> elementos = new ArrayList<>();

    BaseDatos bdd;

//    private Cursor eventos;

    SharedPreferences preferencias;
    SharedPreferences.Editor editor;

    private int idUsuario = 0;

    public ListaEventos() {
        // Required empty public constructor
    }

    public static ListaEventos newInstance(String data) {
        ListaEventos fragment = new ListaEventos();
        Bundle args = new Bundle();
        args.putString("data_evento", data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencias = requireActivity().getSharedPreferences("inicio_sesion", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View vista =  inflater.inflate(R.layout.fragment_lista_eventos, container, false);

        /* Enlazar elementos logicos con los elementos graficos */
        txt_num_pagina = vista.findViewById(R.id.txt_numero);
        btn_anterior = vista.findViewById(R.id.btn_anterior);
        btn_siguiente = vista.findViewById(R.id.btn_siguiente);




        btn_anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagina_actual -= 1;
                if (pagina_actual < 1) {
                    pagina_actual = total_paginas;
                }
                consultarEventos(vista);
            }
        });

        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagina_actual += 1;
                if (pagina_actual > total_paginas) {
                    pagina_actual = 1;
                }
                consultarEventos(vista);
            }
        });

        bdd = new BaseDatos(getContext());
        consultarTotalEventos();
        if (total_paginas == 1) {
            btn_anterior.setEnabled(false);
            btn_siguiente.setEnabled(false);
        }
        consultarEventos(vista);
        return vista;
    }

    private void consultarTotalEventos() {
        int total_eventos = bdd.totalEventos(preferencias.getInt("id_usuario", 0));
        total_paginas = (int) Math.ceil((double) total_eventos / cantidad_mostrar);
        txt_num_pagina.setText(String.format("%d / %d", pagina_actual, total_paginas));
    }

    private void consultarEventos(View vista) {
        elementos.clear();
        Cursor eventos = bdd.buscarEventos(preferencias.getInt("id_usuario", 0), pagina_actual, cantidad_mostrar);
        if (eventos != null && eventos.getCount() > 0 && eventos.moveToFirst()) {
            txt_num_pagina.setText(String.format("%d / %d", pagina_actual, total_paginas));
            do {
                int id = eventos.getInt(0);
                String titulo = eventos.getString(1);
                String descripcion = eventos.getString(2);
                String fecha = eventos.getString(3);
                String hora = eventos.getString(4);
                int fkUsuario = eventos.getInt(7);
                byte[] imagenBlob = eventos.getBlob(5);
                Bitmap imagenBitmap = null;
                if (imagenBlob != null) {
                    imagenBitmap = BitmapFactory.decodeByteArray(imagenBlob, 0, imagenBlob.length);
                }

                elementos.add(new Evento(id, "#775477", titulo, descripcion, fecha, hora, "Activo", fkUsuario, imagenBitmap));

                EventosAdapter eventosAdapter = new EventosAdapter(elementos, getContext(), new EventosAdapter.OnItemClickListener() {
                    @Override
                    public void onIntemClick(Evento item) {
                        moveToDescription(item);
                    }
                });

                RecyclerView recyclerView = vista.findViewById(R.id.lista_eventos);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(eventosAdapter);
            } while (eventos.moveToNext());
            eventos.close();
        } else {
            Toast.makeText(getContext(), "No existen Eventos registrados", Toast.LENGTH_SHORT).show();
        }

    }

    public void moveToDescription(Evento item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("evento_item", item);

        EditarEvento fragment = new EditarEvento();
        fragment.setArguments(bundle);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        // Permite retroceder al fragmento reemplazado
//      transaction.addToBackStack(null);
        transaction.commit();
    }




}
