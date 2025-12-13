package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class DeleteCardRequest {
    @SerializedName("id_tarjeta")
    private final int cardId;

    public DeleteCardRequest(int cardId) {
        this.cardId = cardId;
    }
}
