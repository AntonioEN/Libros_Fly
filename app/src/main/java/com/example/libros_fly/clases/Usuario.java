package com.example.libros_fly.clases;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String nombre;
    private String email;
    private String contrasenna;
    private String id;

    public Usuario() {
    }

    public Usuario(String nombre, String email, String contrasenna, String id) {
        this.nombre = nombre;
        this.email = email;
        this.contrasenna = contrasenna;
        this.id = id;
    }

    public Usuario(String nombre, String id) {
        this.nombre = nombre;
        this.id = id;
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

    public String getContrasenna() {
        return contrasenna;
    }

    public void setContrasenna(String contrasenna) {
        this.contrasenna = contrasenna;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", contrasenna='" + contrasenna + '\'' +
                ", idUsuario=" + id +
                '}';
    }
}
