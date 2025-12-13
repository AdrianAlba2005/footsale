package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class VerifyCodeRequest {
    @SerializedName("email")
    private final String email;

    @SerializedName("code")
    private final String code;

    public VerifyCodeRequest(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
