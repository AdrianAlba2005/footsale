package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class ResetPasswordRequest {
    @SerializedName("email")
    private final String email;

    @SerializedName("code")
    private final String code;

    @SerializedName("password")
    private final String password;

    public ResetPasswordRequest(String email, String code, String password) {
        this.email = email;
        this.code = code;
        this.password = password;
    }
}
