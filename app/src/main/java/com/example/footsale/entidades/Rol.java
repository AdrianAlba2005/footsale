package com.example.footsale.entidades;

import com.google.gson.annotations.SerializedName;

public class Rol {

    @SerializedName("id_rol")
    private int id;

    @SerializedName("nombre_rol")
    private String nombre;

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    // Esto es importante para que el Spinner muestre el nombre del rol
    @Override
    public String toString() {
        return nombre;
    }
}
