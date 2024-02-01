package com.utc.cuentaregresiva.entidades;

import android.graphics.Bitmap;

import java.io.Serializable;

// Implements Serializable -> pasar eventos
public class Evento implements Serializable {
    private int idEvento;
    private String color;
    private String titulo;
    private String descripcion;
    private String fecha;
    private String hora;
    private String estado;
    private int fkUsuario;
    private Bitmap imagen;


    public Evento(int idEvento, String color, String titulo, String descripcion, String fecha, String hora, String estado, int fkUsuario, Bitmap imagen) {
        this.idEvento = idEvento;
        this.color = color;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.fkUsuario = fkUsuario;
        this.imagen = imagen;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public int getFkUsuario() {
        return fkUsuario;
    }

    public void setFkUsuario(int fkUsuario) {
        this.fkUsuario = fkUsuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
