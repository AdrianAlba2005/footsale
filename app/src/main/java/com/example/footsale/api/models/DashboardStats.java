package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class DashboardStats {

    @SerializedName("total_users")
    private int totalUsers;

    @SerializedName("total_products")
    private int totalProducts;

    @SerializedName("total_sales_value")
    private double totalSalesValue;

    public int getTotalUsers() {
        return totalUsers;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public double getTotalSalesValue() {
        return totalSalesValue;
    }
}
