package com.utc.cuentaregresiva.fragmentos;

import android.app.ActivityOptions;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.utc.cuentaregresiva.R;
import com.utc.cuentaregresiva.entidades.BaseDatos;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecoverPassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecoverPassword extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private TextView txt_sub_titulo, txt_instruccion;
    private EditText edt_nombre_usuario, edt_email;
    private Button btn_recuperar;
    private ImageView btn_volver;

    private TextInputLayout txt_nombre_usuario, txt_email;

    private BaseDatos bd;
    private Cursor recuperar;

    public RecoverPassword() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment recoverPassword.
     */
    // TODO: Rename and change types and number of parameters
    public static RecoverPassword newInstance(String param1, String param2) {
        RecoverPassword fragment = new RecoverPassword();
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
        bd = new BaseDatos(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_recover_password, container, false);



        txt_sub_titulo = vista.findViewById(R.id.txt_sub_titulo);
        txt_instruccion = vista.findViewById(R.id.txt_instruccion);
        txt_nombre_usuario = vista.findViewById(R.id.txt_nombre_usuario);
        edt_nombre_usuario = vista.findViewById(R.id.edt_nombre_usuario);
        txt_email = vista.findViewById(R.id.txt_email);
        edt_email = vista.findViewById(R.id.edt_email);

        btn_recuperar = vista.findViewById(R.id.btn_recuperar);
        btn_volver = vista.findViewById(R.id.btn_volver);

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        btn_recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperarCuenta();
            }
        });

        return vista;
    }

    private void recuperarCuenta() {
        String nombre_usuario = edt_nombre_usuario.getText().toString();
        String email = edt_email.getText().toString();
        if (!nombre_usuario.isEmpty() && !email.isEmpty()) {
            recuperar = bd.recuperarCuenta(nombre_usuario, email);
            if (recuperar != null) {
                int idUsuario = recuperar.getInt(0);
                Bundle bundle = new Bundle();
                bundle.putInt("id_usuario", idUsuario);


                NewPassword fragment = new NewPassword();
                fragment.setArguments(bundle);

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                /* Crear el efecto de transicion entre los componentes de los fragmentos */
                transaction.addSharedElement(txt_sub_titulo, "tran_sub_title");
                transaction.addSharedElement(txt_instruccion, "tran_prompt");
                transaction.addSharedElement(txt_nombre_usuario, "tran_nombre_usuario");
                transaction.addSharedElement(txt_email, "tran_email");
                transaction.addSharedElement(btn_recuperar, "tran_boton");
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            } else {
                Toast.makeText(getContext(), "El Usuario con ese Nombre de Usuario o Email no Existe!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
}
