package com.example.footsale.entidades.response;

import com.google.gson.annotations.SerializedName;

public class Conversacion {

    @SerializedName("id_usuario")
    private int idUsuario;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("foto_perfil")
    private String fotoPerfil;

    @SerializedName("ultimo_mensaje")
    private String ultimoMensaje;

    @SerializedName("fecha_ultimo_mensaje")
    private String fechaUltimoMensaje;

    @SerializedName("no_leidos")
    private int noLeidos;

    // NUEVO CAMPO
    @SerializedName("id_emisor_ultimo_mensaje")
    private int idEmisorUltimoMensaje;

    // Getters
    public int getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getFotoPerfil() { return fotoPerfil; }
    public String getUltimoMensaje() { return ultimoMensaje; }
    public String getFechaUltimoMensaje() { return fechaUltimoMensaje; }
    public int getNoLeidos() { return noLeidos; }
    public int getIdEmisorUltimoMensaje() { return idEmisorUltimoMensaje; } // NUEVO GETTER
}
