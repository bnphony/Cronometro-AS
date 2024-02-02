package com.utc.cuentaregresiva.entidades;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class BaseDatos extends SQLiteOpenHelper {

    private static final String nombreBdd = "bdd_cronometro";
    private static final int versionBdd = 2;
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

    // Variable para grabar tipo BLOB
    private ByteArrayOutputStream imagenBlob;
    private byte[] imageInBytes; // Esta variable se guarda en la base de datos

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
//      db.execSQL("ALTER TABLE evento ADD COLUMN imagen_evt BLOB;");
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

    // Proceso 6: Registrar un Evento
    public boolean registrarEvento(String titulo, String descripcion, String fecha, String hora, int idUsuario, Bitmap imagen) {
        SQLiteDatabase miBdd = getWritableDatabase();
        if (miBdd != null) {
            try {
                imageInBytes = null;

                String insertQuery = "INSERT INTO evento(titulo_evt, descripcion_evt, f_final_evt, hora_final_evt, fk_usuario, imagen_evt) VALUES (?, ?, ?, ?, ?, ?)";
                SQLiteStatement statement = miBdd.compileStatement(insertQuery);

                statement.bindString(1, titulo);
                statement.bindString(2, descripcion);
                statement.bindString(3, fecha);
                statement.bindString(4, hora);
                statement.bindLong(5, idUsuario);
                if (imagen != null) {
                    imagenBlob = new ByteArrayOutputStream();
                    imagen.compress(Bitmap.CompressFormat.JPEG, 100, imagenBlob);
                    imageInBytes = imagenBlob.toByteArray();
                    statement.bindBlob(6, imageInBytes);
                } else {
                    statement.bindNull(6);
                }

                statement.executeInsert();
                statement.close();
                miBdd.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Proceso 7: Conseguir Numero de Eventos Registrados
    public int conseguirCountEventos(int fkUsuario) {
        SQLiteDatabase idBdd = getReadableDatabase();
        int id_maximo = 0;
        Cursor cursor = idBdd.rawQuery("SELECT count(id_evt)+1 FROM evento WHERE fk_usuario = " + fkUsuario, null);
        if (cursor.moveToFirst()) {
            idBdd.close();
            id_maximo = cursor.getInt(0);
        }
        return id_maximo;
    }

    // Proceso 8: Consultar la lista de eventos registrados
    public Cursor buscarEventos(int idUsuario, int pagina, int cantidad) {
        int inicio = 0;
        inicio = (cantidad * (pagina - 1));
        System.out.println(String.format("INICIO : %d, CANTIDAD: %d", inicio, cantidad));
        SQLiteDatabase miBdd = getReadableDatabase();
        Cursor eventos = miBdd.rawQuery("SELECT * FROM evento WHERE fk_usuario = " + idUsuario + " " +
                "LIMIT "+inicio+", "+cantidad, null);
        if (eventos.moveToFirst()) {

//            miBdd.close();
            return eventos;
        } else {
            return null;
        }
    }
    public int totalEventos(int idUsuario) {
        SQLiteDatabase miBdd = getReadableDatabase();
        Cursor eventos = miBdd.rawQuery("SELECT COUNT(*) FROM evento WHERE fk_usuario = " + idUsuario, null);
        int totalEventos = 0;
        if (eventos != null && eventos.moveToFirst()) {
            totalEventos = eventos.getInt(0);
            eventos.close();
            return totalEventos;
        }
        return 0;
    }

    // Proceso 9: Actualizar informacion de un evento
    public boolean actualizarEvento(int idEvento, String titulo, String descripcion, String fecha, String hora, Bitmap imagen) {
        SQLiteDatabase bdd = getWritableDatabase();
        if (bdd != null) {
            try {
                imageInBytes = null;

                String updateQuery = "UPDATE evento SET " +
                        "titulo_evt = ?, " +
                        "descripcion_evt = ?, " +
                        "f_final_evt = ?, " +
                        "hora_final_evt = ?, " +
                        "imagen_evt = ? " +
                        "WHERE id_evt = ?";
                SQLiteStatement statement = bdd.compileStatement(updateQuery);

                statement.bindString(1, titulo);
                statement.bindString(2, descripcion);
                statement.bindString(3, fecha);
                statement.bindString(4, hora);
                if (imagen != null) {
                    imagenBlob = new ByteArrayOutputStream();
                    imagen.compress(Bitmap.CompressFormat.JPEG, 100, imagenBlob);
                    imageInBytes = imagenBlob.toByteArray();
                    statement.bindBlob(5, imageInBytes);
                } else {
                    statement.bindNull(5);
                }
                statement.bindLong(6, idEvento);

                statement.executeUpdateDelete();
                statement.close();
                bdd.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Proceso 10: Eliminar un Evento
    public boolean eliminarEvento(int idEvento) {
        SQLiteDatabase bdd = getWritableDatabase();
        if (bdd != null) {
            bdd.execSQL("DELETE FROM evento WHERE id_evt = " + idEvento);
            bdd.close();
            return true;
        }
        return false;
    }

    // Proceso 11: Obtener la informacion de un Usuario
    public Cursor obtenerUsuario(int idUsuario) {
        SQLiteDatabase miBdd = getReadableDatabase();
        Cursor usuario = miBdd.rawQuery("SELECT * FROM usuario WHERE id_usu = " + idUsuario, null);
        if (usuario.moveToFirst()) {
            return usuario;
        } else {
            return null;
        }
    }

    // Proceso 12: Actualizar informacion del Usuario, excepto el Password
    public boolean actualizarPerfil(int idUsuario, String nombre, String nombre_usuario, String email) {
        SQLiteDatabase bdd = getWritableDatabase();
        if (bdd != null) {
            bdd.execSQL("UPDATE usuario SET " +
                    "nombre_usu = '"+nombre+"', " +
                    "nombre_usuario_usu = '"+nombre_usuario+"', " +
                    "email_usu = '"+email+"' " +
                    "WHERE id_usu = " + idUsuario);
            bdd.close();
            return true;
        }
        return false;
    }

    // Proceso 13: Comprobar si el nombre de usuario esta disponible cuando se actualiza un Usuario
    public boolean validarNombreUsuarioExistente(int idUsuario, String nombre_usuario) {
        SQLiteDatabase nombreBdd = getReadableDatabase();
        Cursor cursor = nombreBdd.rawQuery("SELECT nombre_usuario_usu FROM usuario " +
                "WHERE nombre_usuario_usu = '"+nombre_usuario+"' AND id_usu != " + idUsuario, null);
        if (cursor.moveToFirst()) {
            nombreBdd.close();
            return true;
        }
        return false;
    }

    // Proceso 14: Actualizar Password
    public boolean actualizarPassword(int idUsuario, String password) {
        SQLiteDatabase bdd = getWritableDatabase();
        if (bdd != null) {
            bdd.execSQL("UPDATE usuario set " +
                    "password_usu = '"+password+"' " +
                    "WHERE id_usu = " + idUsuario);
            bdd.close();
            return true;
        }
        return false;
    }

    // Proceso 15: Recuperar Cuenta
    public Cursor recuperarCuenta(String nombre_usuario, String email) {
        SQLiteDatabase miBdd = getReadableDatabase();
        Cursor usuario = miBdd.rawQuery("SELECT id_usu FROM usuario " +
                "WHERE nombre_usuario_usu = '"+nombre_usuario+"' and email_usu = '"+email+"'", null);
        if (usuario.moveToFirst()) {
            return usuario;
        }
        return null;
    }




}
