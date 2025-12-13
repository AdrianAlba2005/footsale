package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class SendMessageRequest {
    
    @SerializedName("message") // Your PHP script expects "message" in send_message(), but in function it maps it to $content
    private String message;
    
    @SerializedName("receiver_id") // Your PHP expects "receiver_id"
    private int receiver_id;
    
    @SerializedName("product_id") // Your PHP expects "product_id"
    private int product_id;

    public SendMessageRequest(String message, int receiver_id, int product_id) {
        this.message = message;
        this.receiver_id = receiver_id;
        this.product_id = product_id;
    }
}