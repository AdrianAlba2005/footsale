package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ProcessCardOrderRequest {
    @SerializedName("products")
    private final List<ProductQuantity> products;

    @SerializedName("card_id")
    private final int cardId;

    @SerializedName("summary")
    private final CheckoutSummary summary;

    public ProcessCardOrderRequest(List<ProductQuantity> products, int cardId, CheckoutSummary summary) {
        this.products = products;
        this.cardId = cardId;
        this.summary = summary;
    }
}
