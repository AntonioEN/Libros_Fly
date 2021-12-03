package com.example.libros_fly.clases;

import java.io.Serializable;

public class Prestamo implements Serializable {

    private String isbn;
    private String idUsuario;

    public Prestamo(String isbn, String idUsuario) {
        this.isbn = isbn;
        this.idUsuario = idUsuario;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "isbn='" + isbn + '\'' +
                ", idUsuario='" + idUsuario + '\'' +
                '}';
    }
}
