package com.example.footsale.entidades.response;

import com.google.gson.annotations.SerializedName;

public class LastMessage {

    @SerializedName("id_mensaje")
    private int idMensaje;

    @SerializedName("contenido")
    private String contenido;

    @SerializedName("fecha_envio")
    private String fechaEnvio;

    @SerializedName("id_producto")
    private int idProducto;

    @SerializedName("id_emisor")
    private int idEmisor; // <-- CAMPO NUEVO

    // Getters
    public int getIdMensaje() { return idMensaje; }
    public String getContenido() { return contenido; }
    public String getFechaEnvio() { return fechaEnvio; }
    public int getIdProducto() { return idProducto; }
    public int getIdEmisor() { return idEmisor; } // <-- GETTER NUEVO
}