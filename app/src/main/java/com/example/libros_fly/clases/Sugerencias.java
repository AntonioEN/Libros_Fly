package com.example.libros_fly.clases;

import java.io.Serializable;

public class Sugerencias implements Serializable {

    private String idUsuario;
    private String tituloLibro;

    public Sugerencias(String idUsuario, String tituloLibro) {
        this.idUsuario = idUsuario;
        this.tituloLibro = tituloLibro;
    }


    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTituloLibro() {
        return tituloLibro;
    }

    public void setTituloLibro(String tituloLibro) {
        this.tituloLibro = tituloLibro;
    }

    @Override
    public String toString() {
        return "Sugerencias{" +
                ", idUsuario='" + idUsuario + '\'' +
                ", tituloLibro='" + tituloLibro + '\'' +
                '}';
    }
}
