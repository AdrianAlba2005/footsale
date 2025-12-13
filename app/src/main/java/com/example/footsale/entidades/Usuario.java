package com.example.footsale.entidades;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Usuario implements Serializable {

    @SerializedName("id_usuario")
    private int idUsuario;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("email")
    private String email;

    @SerializedName("fecha_registro")
    private String fechaRegistro;

    @SerializedName("saldo")
    private double saldo;

    @SerializedName("rol")
    private String rol;

    @SerializedName("foto_perfil")
    private String fotoPerfil;

    @SerializedName("nacionalidad")
    private String nacionalidad;

    @SerializedName("telefono")
    private String telefono;

    @SerializedName("calle")
    private String calle;

    @SerializedName("ciudad")
    private String ciudad;

    // --- GETTERS ---
    public int getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getFechaRegistro() { return fechaRegistro; }
    public double getSaldo() { return saldo; }
    public String getRol() { return rol; }
    public String getFotoPerfil() { return fotoPerfil; }
    public String getNacionalidad() { return nacionalidad; }
    public String getTelefono() { return telefono; }
    public String getCalle() { return calle; }
    public String getCiudad() { return ciudad; }

    // --- SETTERS ---
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setCalle(String calle) { this.calle = calle; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
}
