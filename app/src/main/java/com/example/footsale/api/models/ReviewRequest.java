package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class ReviewRequest {
    @SerializedName("id_producto")
    private final int idProducto;

    @SerializedName("calificacion")
    private final int calificacion;

    @SerializedName("comentario")
    private final String comentario;

    public ReviewRequest(int idProducto, int calificacion, String comentario) {
        this.idProducto = idProducto;
        this.calificacion = calificacion;
        this.comentario = comentario;
    }
}
