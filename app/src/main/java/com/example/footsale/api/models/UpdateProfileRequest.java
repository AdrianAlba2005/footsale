package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileRequest {
    @SerializedName("nombre")
    private final String nombre;

    @SerializedName("nacionalidad")
    private final String nacionalidad;

    @SerializedName("telefono")
    private final String telefono;

    @SerializedName("calle")
    private final String calle;

    @SerializedName("ciudad")
    private final String ciudad;

    public UpdateProfileRequest(String nombre, String nacionalidad, String telefono, String calle, String ciudad) {
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.telefono = telefono;
        this.calle = calle;
        this.ciudad = ciudad;
    }
}
