package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class CheckoutSummary {

    @SerializedName("subtotal")
    private double subtotal;

    @SerializedName("shipping_cost")
    private double shippingCost;

    @SerializedName("vat_amount")
    private double vatAmount;

    @SerializedName("total")
    private double total;

    // Getters
    public double getSubtotal() { return subtotal; }
    public double getShippingCost() { return shippingCost; }
    public double getVatAmount() { return vatAmount; }
    public double getTotal() { return total; }
}
