package com.utc.cuentaregresiva.fragmentos;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.utc.cuentaregresiva.R;
import com.utc.cuentaregresiva.entidades.BaseDatos;

import java.util.ArrayList;


public class ListaEventos extends Fragment {

    private ListView lista_eventos;
    private ArrayList<String> listaEventos = new ArrayList<>();

    BaseDatos bdd;

    Cursor eventos;

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
        View vista =  inflater.inflate(R.layout.fragment_lista_eventos, container, false);

        // Mapear o relacionar los componentes logicos con los componentes graficos
        lista_eventos = (ListView) vista.findViewById(R.id.lista_eventos);
        bdd = new BaseDatos(getContext());
        consultarEventos();

        lista_eventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                eventos.moveToPosition(position);
                int idEvento = eventos.getInt(0);
                String tituloEvento = eventos.getString(1);
                String descripcionEvento = eventos.getString(2);
                String fechaLimite = eventos.getString(3);
                String horaLimite = eventos.getString(4);
                int fkUsuario = eventos.getInt(5);

                Bundle bundle = new Bundle();
                bundle.putInt("id_evento", idEvento);
                bundle.putString("titulo", tituloEvento);
                bundle.putString("descripcion", descripcionEvento);
                bundle.putString("fecha", fechaLimite);
                bundle.putString("hora", horaLimite);
                bundle.putInt("fk_usuario", fkUsuario);

                EditarEvento fragment = new EditarEvento();
                fragment.setArguments(bundle);

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                // Permite retroceder al fragmento reemplazado
//                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return vista;
    }


    private void consultarEventos() {
        listaEventos.clear();
        eventos = bdd.buscarEventos(preferencias.getInt("id_usuario", 0));
        if (eventos != null) {
            do {
                int id = eventos.getInt(0);
                String titulo = eventos.getString(1);
                String descripcion = eventos.getString(2);
                String fecha = eventos.getString(3);
                String hora = eventos.getString(4);
                listaEventos.add(id + "\nTitulo: " + titulo);
                ArrayAdapter<String> adaptadorEventos = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listaEventos);
                // Asignar el adaptador a lista de Productos
                lista_eventos.setAdapter(adaptadorEventos);
            } while (eventos.moveToNext());
        } else {
            Toast.makeText(getContext(), "No existen Eventos registrados", Toast.LENGTH_SHORT).show();
        }
    }
}
