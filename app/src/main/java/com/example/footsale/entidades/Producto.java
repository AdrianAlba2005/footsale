package com.example.footsale.entidades;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Producto {

    @SerializedName("id_producto")
    private int idProducto;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("precio")
    private double precio;

    @SerializedName("cantidad")
    private int cantidad;

    @SerializedName("estado")
    private String estado;

    @SerializedName("imagen_principal")
    private String imagenPrincipal;

    // NUEVO: Campo para la lista de imágenes
    @SerializedName("imagenes")
    private List<String> imagenes;

    @SerializedName("usuario")
    private Usuario usuario;

    @SerializedName("categoria")
    private Categoria categoria;

    @SerializedName("is_favorited")
    private boolean isFavorited;

    // Getters
    public int getIdProducto() { return idProducto; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public int getCantidad() { return cantidad; }
    public String getEstado() { return estado; }
    public String getImagenPrincipal() { return imagenPrincipal; }
    public List<String> getImagenes() { return imagenes; } // Getter nuevo
    public Usuario getUsuario() { return usuario; }
    public Categoria getCategoria() { return categoria; }
    public boolean isFavorited() { return isFavorited; }

    // Setters
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setImagenPrincipal(String imagenPrincipal) { this.imagenPrincipal = imagenPrincipal; }
    public void setImagenes(List<String> imagenes) { this.imagenes = imagenes; } // Setter nuevo
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public void setFavorited(boolean favorited) { isFavorited = favorited; }
}