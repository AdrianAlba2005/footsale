package com.example.footsale.entidades;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class DetallePedido implements Serializable {

    @SerializedName("id_producto")
    private int idProducto;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("imagen")
    private String imagen;

    @SerializedName("cantidad")
    private Integer cantidad;

    @SerializedName("precio_unitario")
    private Double precioUnitario;

    // --- Getters y Setters ---

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}
