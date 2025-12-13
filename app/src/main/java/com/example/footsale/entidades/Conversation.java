package com.example.footsale.entidades;

import com.google.gson.annotations.SerializedName;

public class Conversation {

    @SerializedName("id_usuario")
    private int userId;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("foto_perfil")
    private String fotoPerfil;

    @SerializedName("last_message")
    private String lastMessage;

    @SerializedName("last_message_time")
    private String lastMessageTime;

    @SerializedName("unread_count")
    private int unreadCount;

    // Getters
    public int getUserId() {
        return userId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public int getUnreadCount() {
        return unreadCount;
    }
}
