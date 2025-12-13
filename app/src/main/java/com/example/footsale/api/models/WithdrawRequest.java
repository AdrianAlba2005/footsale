package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class WithdrawRequest {
    @SerializedName("card_id")
    private final int cardId;

    @SerializedName("amount")
    private final double amount;

    public WithdrawRequest(int cardId, double amount) {
        this.cardId = cardId;
        this.amount = amount;
    }
}
