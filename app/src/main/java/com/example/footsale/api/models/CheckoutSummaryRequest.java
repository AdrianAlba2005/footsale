package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CheckoutSummaryRequest {
    @SerializedName("products")
    private final List<ProductQuantity> products;

    public CheckoutSummaryRequest(List<ProductQuantity> products) {
        this.products = products;
    }
}
