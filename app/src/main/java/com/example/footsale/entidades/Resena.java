package com.example.footsale.entidades;

import com.google.gson.annotations.SerializedName;

public class Resena {

    @SerializedName("id_resena")
    private int id;

    @SerializedName("id_usuario_autor")
    private int idUsuarioAutor;

    @SerializedName("nombre_autor")
    private String nombreAutor;

    @SerializedName("puntuacion")
    private float puntuacion;

    @SerializedName("comentario")
    private String comentario;
    
    @SerializedName("fecha_reseña")
    private String fechaResena;

    public int getId() { return id; }
    public int getIdUsuarioAutor() { return idUsuarioAutor; }
    public String getNombreAutor() { return nombreAutor; }
    public float getPuntuacion() { return puntuacion; }
    public String getComentario() { return comentario; }
    public String getFechaResena() { return fechaResena; }
}
