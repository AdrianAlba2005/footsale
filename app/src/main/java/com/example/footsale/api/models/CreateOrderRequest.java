package com.example.footsale.api.models;

import java.util.List;

public class CreateOrderRequest {
    private List<Integer> productIds;

    public CreateOrderRequest(List<Integer> productIds) {
        this.productIds = productIds;
    }
}