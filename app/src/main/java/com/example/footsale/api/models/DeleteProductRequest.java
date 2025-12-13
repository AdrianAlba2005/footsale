package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class DeleteProductRequest {
    @SerializedName("id_producto")
    private final int id_producto;

    public DeleteProductRequest(int productId) {
        this.id_producto = productId;
    }
}
