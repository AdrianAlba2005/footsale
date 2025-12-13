package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class ToggleFavoriteRequest {
    @SerializedName("id_producto")
    private final int id_producto;

    public ToggleFavoriteRequest(int productId) {
        this.id_producto = productId;
    }
}
