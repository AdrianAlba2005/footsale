package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

// Este modelo representa los datos para crear un producto.
// Aunque la petición final es multipart, se usa para mantener la estructura.
public class AddProductRequest {

    @SerializedName("titulo")
    private final String titulo;

    @SerializedName("descripcion")
    private final String descripcion;

    @SerializedName("precio")
    private final double precio;

    @SerializedName("cantidad")
    private final int cantidad;

    @SerializedName("id_categoria")
    private final int idCategoria;

    public AddProductRequest(String titulo, String descripcion, double precio, int cantidad, int idCategoria) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
        this.idCategoria = idCategoria;
    }
}
