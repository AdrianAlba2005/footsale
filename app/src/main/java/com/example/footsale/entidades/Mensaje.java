package com.example.footsale.entidades;

import com.google.gson.annotations.SerializedName;

public class Mensaje {

    @SerializedName("id_mensaje")
    private Integer idMensaje;

    @SerializedName("contenido")
    private String contenido;

    @SerializedName("fecha_envio")
    private String fechaEnvio;

    @SerializedName("id_emisor")
    private Integer idEmisor;

    @SerializedName("id_receptor")
    private Integer idReceptor;

    @SerializedName("is_read")
    private boolean isRead;

    // Nuevo campo para el nombre del producto
    @SerializedName("nombre_producto")
    private String nombreProducto;

    // Campo id_producto que faltaba para sincronizar con BD
    @SerializedName("id_producto")
    private Integer idProducto;

    // Getters y Setters

    public Integer getIdMensaje() { return idMensaje; }
    public void setIdMensaje(Integer idMensaje) { this.idMensaje = idMensaje; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public String getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(String fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public Integer getIdEmisor() { return idEmisor; }
    public void setIdEmisor(Integer idEmisor) { this.idEmisor = idEmisor; }

    public Integer getIdReceptor() { return idReceptor; }
    public void setIdReceptor(Integer idReceptor) { this.idReceptor = idReceptor; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
}