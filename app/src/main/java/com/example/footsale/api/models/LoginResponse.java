package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("token")
    private String token;

    // Getter que faltaba
    public String getToken() {
        return token;
    }
}
