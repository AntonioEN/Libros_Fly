package com.example.libros_fly.clases;

import java.io.Serializable;
import java.util.Date;

public class Libros implements Serializable {

    private String titulo;
    private String autor;
    private String sinopsis;
    private String ISBN;
    private String genero;
    private String stock;
    private String fotoPortada;

    public Libros() {

    }

    public Libros(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
    }

    public Libros(String titulo, String autor, String ISBN) {
        this.titulo = titulo;
        this.autor = autor;
        this.ISBN = ISBN;
    }

    public Libros(String titulo, String autor, String ISBN, String sinopsis) {
        this.titulo = titulo;
        this.autor = autor;
        this.ISBN = ISBN;
        this.sinopsis = sinopsis;
    }

    public Libros(String titulo, String autor, String sinopsis, String ISBN, String genero, String stock, String fotoPortada) {
        this.titulo = titulo;
        this.autor = autor;
        this.sinopsis = sinopsis;
        this.ISBN = ISBN;
        this.genero = genero;
        this.stock = stock;
        this.fotoPortada = fotoPortada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getGenero() { return genero; }

    public void setGenero(String genero) { this.genero = genero; }

    public String getStock() { return stock; }

    public void setStock(String stock) { this.stock = stock; }

    public String getFotoPortada() {
        return fotoPortada;
    }

    public void setFotoPortada(String fotoPortada) {
        this.fotoPortada = fotoPortada;
    }

    @Override
    public String toString() {
        return "Libros{" +
                "titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", sinopsis='" + sinopsis + '\'' +
                ", ISBN='" + ISBN + '\'' +
                ", genero='" + genero + '\'' +
                ", stock='" + stock + '\'' +
                ", fotoPortada='" + fotoPortada + '\'' +
                '}';
    }
}

