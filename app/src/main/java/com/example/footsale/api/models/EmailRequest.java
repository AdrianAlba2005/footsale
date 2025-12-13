package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class EmailRequest {
    @SerializedName("email")
    private final String email;

    public EmailRequest(String email) {
        this.email = email;
    }
}
