package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class DashboardStats {

    @SerializedName("total_users")
    private int totalUsers;

    @SerializedName("total_products")
    private int totalProducts;

    @SerializedName("total_orders")
    private int totalOrders;

    @SerializedName("total_sales_value")
    private double totalSalesValue;

    @SerializedName("total_money")
    private double totalMoneyAlternative;

    public int getTotalUsers() {
        return totalUsers;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public int getTotalOrders() { return totalOrders; }

    public double getTotalSalesValue() {
        // Devuelve el valor que no sea 0, priorizando totalSalesValue
        return (totalSalesValue != 0) ? totalSalesValue : totalMoneyAlternative;
    }
}
