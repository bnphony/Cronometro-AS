package com.utc.cuentaregresiva.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.utc.cuentaregresiva.R;
import com.utc.cuentaregresiva.entidades.BaseDatos;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewPassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewPassword extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText edt_password, edt_confirm_password;
    private Button btn_actualizar;
    private ImageView btn_volver;
    private TextView btn_iniciar_sesion;

    private BaseDatos bd;

    private int idUsuario;


    public NewPassword() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment newPassword.
     */
    // TODO: Rename and change types and number of parameters
    public static NewPassword newInstance(String param1, String param2) {
        NewPassword fragment = new NewPassword();
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

            idUsuario = getArguments().getInt("id_usuario");
        }

        bd = new BaseDatos(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_new_password, container, false);

        edt_password = vista.findViewById(R.id.edt_password);
        edt_confirm_password = vista.findViewById(R.id.edt_confirm_password);

        btn_actualizar = vista.findViewById(R.id.btn_actualizar);
        btn_volver = vista.findViewById(R.id.btn_volver);
        btn_iniciar_sesion = vista.findViewById(R.id.btn_iniciar_sesion);

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });

        btn_iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });


        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarPassword();
            }
        });


        return vista;
    }

    private void actualizarPassword() {
        String password = edt_password.getText().toString();
        String confirmacion = edt_confirm_password.getText().toString();

        if (!password.isEmpty() && !confirmacion.isEmpty()) {
            if (password.equals(confirmacion)) {
                bd.actualizarPassword(idUsuario, password);
                Toast.makeText(getContext(), "Password actualizado correctamente!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Los Passwords no Coinciden!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Debe llenar todos los campos!", Toast.LENGTH_SHORT).show();
        }
    }
}
