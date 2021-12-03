package com.example.libros_fly.clases;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Reservas implements Serializable {

    private String ISBN;
    private String id_usuario;
    private String fecha_reserva_fin;
    private String titulo;

    public Reservas() {
    }

    public Reservas(String ISBN, String id_usuario) {
        this.ISBN = ISBN;
        this.id_usuario = id_usuario;
    }

    public Reservas(String ISBN, String id_usuario, String fecha_reserva_fin, String titulo) {
        this.ISBN = ISBN;
        this.id_usuario = id_usuario;
        this.fecha_reserva_fin = fecha_reserva_fin;
        this.titulo = titulo;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }


    public String getFecha_reserva_fin() {
        return fecha_reserva_fin;
    }

    public void setFecha_reserva_fin(String fecha_reserva_fin) {
        this.fecha_reserva_fin = fecha_reserva_fin;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public String toString() {
        return "Reservas{" +
                "ISBN='" + ISBN + '\'' +
                ", id_usuario='" + id_usuario + '\'' +
                ", fecha_reserva_fin='" + fecha_reserva_fin + '\'' +
                ", titulo='" + titulo + '\'' +
                '}';
    }
}
