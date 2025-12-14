package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class BanUserRequest {
    @SerializedName("id_usuario")
    private int idUsuario;

    public BanUserRequest(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }
}
