package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class ProductQuantity {
    @SerializedName("id_producto")
    private final int idProducto;

    @SerializedName("quantity")
    private final int quantity;

    public ProductQuantity(int productId, int quantity) {
        this.idProducto = productId;
        this.quantity = quantity;
    }
}
