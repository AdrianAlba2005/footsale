package com.example.footsale.entidades;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Pedido implements Serializable {

    @SerializedName("id_pedido")
    private Integer idPedido;

    @SerializedName("fecha")
    private String fecha;

    @SerializedName("total")
    private Double total;

    @SerializedName("estado")
    private String estado;

    @SerializedName("usuario")
    private Usuario usuario;

    @SerializedName("productos")
    private List<DetallePedido> detalles;

    // --- Getters y Setters ---

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }
}
