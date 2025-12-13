package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class AddCardRequest {
    @SerializedName("card_number")
    private final String cardNumber;

    @SerializedName("expiry_month")
    private final String expiryMonth;

    @SerializedName("expiry_year")
    private final String expiryYear;

    @SerializedName("cvv")
    private final String cvv;

    public AddCardRequest(String cardNumber, String expiryMonth, String expiryYear, String cvv) {
        this.cardNumber = cardNumber;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.cvv = cvv;
    }
}
