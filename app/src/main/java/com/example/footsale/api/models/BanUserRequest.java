package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class BanUserRequest {
    @SerializedName("id_usuario")
    private final int userId;

    public BanUserRequest(int userId) {
        this.userId = userId;
    }
}
