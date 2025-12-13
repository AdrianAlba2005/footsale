package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("nombre")
    private final String nombre;

    @SerializedName("email")
    private final String email;

    @SerializedName("password")
    private final String password;

    public RegisterRequest(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }
}
