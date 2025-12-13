package com.example.footsale.utils;

import com.example.footsale.entidades.Producto;

public class CartItem {
    private final Producto product;
    private int quantity;

    public CartItem(Producto product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Producto getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
