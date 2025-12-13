package com.example.footsale.entidades;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("id_mensaje")
    private int id;

    @SerializedName("id_remitente")
    private int senderId;

    @SerializedName("id_destinatario")
    private int receiverId;

    @SerializedName("mensaje")
    private String message;

    @SerializedName("timestamp")
    private String timestamp;

    // Cambiado de boolean a int para evitar errores de parseo con Gson (MySQL devuelve 0/1)
    @SerializedName("leido")
    private int isRead; 

    public int getId() { return id; }
    public int getSenderId() { return senderId; }
    public int getReceiverId() { return receiverId; }
    public String getMessage() { return message; }
    public String getTimestamp() { return timestamp; }
    
    public boolean isRead() { 
        return isRead == 1; 
    }
}
