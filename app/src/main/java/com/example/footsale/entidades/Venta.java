package com.example.footsale.entidades;

import com.google.gson.annotations.SerializedName;

public class Venta {
    @SerializedName("fecha")
    private String fecha;

    @SerializedName("total")
    private double total;

    @SerializedName("cantidad")
    private int cantidad;

    @SerializedName("nombre_producto")
    private String nombreProducto;

    @SerializedName("nombre_comprador")
    private String nombreComprador;

    // Getters
    public String getFecha() { return fecha; }
    public double getTotal() { return total; }
    public int getCantidad() { return cantidad; }
    public String getNombreProducto() { return nombreProducto; }
    public String getNombreComprador() { return nombreComprador; }
}
