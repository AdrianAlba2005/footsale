package com.example.footsale.entidades;

import com.google.gson.annotations.SerializedName;

public class Resena {

    @SerializedName("id_reseña")
    private int id;

    @SerializedName("puntuacion")
    private int puntuacion;

    @SerializedName("comentario")
    private String comentario;

    @SerializedName("id_usuario_autor")
    private int idUsuarioAutor;

    @SerializedName("autor")
    private String nombreAutor;

    @SerializedName("foto_autor")
    private String fotoAutor;

    @SerializedName("fecha_reseña") // Coincidir con el JSON
    private String fechaResena;

    // Getters
    public int getId() { return id; }
    public int getPuntuacion() { return puntuacion; }
    public String getComentario() { return comentario; }
    public int getIdUsuarioAutor() { return idUsuarioAutor; }
    public String getNombreAutor() { return nombreAutor; }
    public String getFotoAutor() { return fotoAutor; }
    public String getFechaResena() { return fechaResena; } // Método añadido
}
