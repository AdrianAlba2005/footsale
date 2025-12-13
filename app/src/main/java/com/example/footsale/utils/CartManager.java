package com.example.footsale.utils;

import com.example.footsale.entidades.Producto;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CartManager {

    private static CartManager instance;
    private final Map<Integer, CartItem> cartItems = new LinkedHashMap<>();

    private CartManager() {}

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(Producto product, int quantity) {
        if (cartItems.containsKey(product.getIdProducto())) {
            CartItem item = cartItems.get(product.getIdProducto());
            if (item != null) {
                item.setQuantity(item.getQuantity() + quantity);
            }
        } else {
            cartItems.put(product.getIdProducto(), new CartItem(product, quantity));
        }
    }

    public void removeFromCart(int productId) {
        cartItems.remove(productId);
    }

    public void updateQuantity(int productId, int newQuantity) {
        CartItem item = cartItems.get(productId);
        if (item != null) {
            if (newQuantity > 0) {
                item.setQuantity(newQuantity);
            } else {
                removeFromCart(productId);
            }
        }
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems.values());
    }

    public double getTotal() {
        double total = 0;
        for (CartItem item : cartItems.values()) {
            total += item.getProduct().getPrecio() * item.getQuantity();
        }
        return total;
    }

    public void clearCart() {
        cartItems.clear();
    }
}
