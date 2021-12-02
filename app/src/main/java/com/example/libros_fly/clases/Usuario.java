package com.example.libros_fly.clases;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String nombre;
    private String email;
    private String id;
    private String nick;


    public Usuario(String nombre, String email, String id, String nick) {
        this.nombre = nombre;
        this.email = email;
        this.id = id;
        this.nick = nick;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }


    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                ", nick='" + nick + '}';
    }
}
