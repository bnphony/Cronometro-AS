package com.utc.cuentaregresiva.fragmentos;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
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

    public ListaEventos() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

            }
        });

        return vista;
    }

    private void consultarEventos() {
        listaEventos.clear();
        eventos = bdd.buscarEventos();
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
