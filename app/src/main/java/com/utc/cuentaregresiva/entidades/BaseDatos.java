package com.utc.cuentaregresiva.entidades;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper {

    private static final String nombreBdd = "bdd_cronometro";
    private static final int versionBdd = 1;
    private static final String tablaUsuario = "CREATE TABLE usuario(" +
            "id_usu integer primary key autoincrement, " +
            "nombre_usu text, " +
            "nombre_usuario_usu text, " +
            "email_usu text, " +
            "password_usu text" +
            ");";
    private static final String tablaEvento = "CREATE TABLE evento(" +
            "id_evt integer primary key autoincrement, " +
            "titulo_evt text, " +
            "descripcion_evt text, " +
            "f_final_evt text, " +
            "hora_final_evt text, " +
            "fk_usuario integer, " +
            "foreign key(fk_usuario) references usuario(id_usu)" +
            ");";

    // Constructor
    public BaseDatos(Context contexto) {
        super(contexto, nombreBdd, null, versionBdd);
    }

    // Proceso 1: Metodo que se ejecuta automaticamente cuando se construye la clase de la Base de Datos
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tablaUsuario);
        db.execSQL(tablaEvento);
    }

    // Proceso 2: Metodo que se ejecuta automaticamente cuando se detectan cambios en la Base de Datos
    // Si se cambia "versionBdd" se ejecuta este metodo
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL(tablaUsuario);
        db.execSQL("DROP TABLE IF EXISTS evento");
        db.execSQL(tablaEvento);
    }

    // Proceso 3: metodo para insertar datos dentro de la tabla usuario, retorna true cuando inserta o false cuando hay algun error
    public boolean agregarUsuario(String nombre, String nombre_usuario, String email, String password) {
        SQLiteDatabase miBdd = getWritableDatabase();
        if (miBdd != null) {
            miBdd.execSQL("INSERT INTO usuario(nombre_usu, nombre_usuario_usu, email_usu, password_usu) VALUES (" +
                    "'"+nombre+"', '"+nombre_usuario+"', '"+email+"', '"+password+"'" +
                    ");");
            miBdd.close();
            return true;
        }
        return false;
    }

    // Proceso 4: Iniciar Sesion
    public Cursor validarUsuario(String nombre_usuario, String password) {
        SQLiteDatabase miBdd = getWritableDatabase();
        Cursor usuario = miBdd.rawQuery("SELECT * FROM usuario WHERE " +
                "nombre_usuario_usu = '"+nombre_usuario+"' and password_usu = '"+password+"';", null);
        if (usuario.moveToFirst()) {
            return usuario;
        } else {
            return  null;
        }
    }

    // Proceso 5: Comprobar si el nombre de usuario esta disponible
    public boolean validarNombreUsuario(String nombre_usuario) {
        SQLiteDatabase nombreBdd = getReadableDatabase();
        Cursor cursor = nombreBdd.rawQuery("SELECT nombre_usuario_usu FROM usuario " +
                "WHERE nombre_usuario_usu = '"+nombre_usuario+"';", null);
        if (cursor.moveToFirst()) {
            nombreBdd.close();
            return true;
        }
        return false;
    }
}
