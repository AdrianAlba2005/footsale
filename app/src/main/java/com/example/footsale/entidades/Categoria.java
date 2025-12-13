package com.example.footsale.entidades;

import com.google.gson.annotations.SerializedName;

public class Categoria {

    @SerializedName("id_categoria")
    private int id_categoria;

    @SerializedName("nombre")
    private String nombre;

    public int getId_categoria() {
        return id_categoria;
    }

    public String getNombre() {
        return nombre;
    }
}
