package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class MarkAsReadRequest {
    @SerializedName("sender_id")
    private int senderId;

    public MarkAsReadRequest(int senderId) {
        this.senderId = senderId;
    }
}
