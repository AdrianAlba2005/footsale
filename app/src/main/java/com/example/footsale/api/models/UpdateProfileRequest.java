package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileRequest {
    @SerializedName("nombre")
    private final String nombre;

    @SerializedName("email")
    private final String email;

    public UpdateProfileRequest(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }
}
