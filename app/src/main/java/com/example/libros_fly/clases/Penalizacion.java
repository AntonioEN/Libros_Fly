package com.example.libros_fly.clases;

import java.io.Serializable;

public class Penalizacion implements Serializable {

    private String id;
    private String fechaTopePenalizacion;

    public Penalizacion(String id, String fechaTopePenalizacion) {
        this.id = id;
        this.fechaTopePenalizacion = fechaTopePenalizacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFechaTopePenalizacion() {
        return fechaTopePenalizacion;
    }

    public void setFechaTopePenalizacion(String fechaTopePenalizacion) {
        this.fechaTopePenalizacion = fechaTopePenalizacion;
    }

    @Override
    public String toString() {
        return "Penalizacion{" +
                "id='" + id + '\'' +
                ", fechaTopePenalizacion='" + fechaTopePenalizacion + '\'' +
                '}';
    }
}
