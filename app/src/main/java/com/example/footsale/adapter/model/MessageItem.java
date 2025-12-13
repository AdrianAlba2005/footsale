package com.example.footsale.adapter.model;

import com.example.footsale.entidades.Mensaje;

public class MessageItem extends ChatItem {
    private Mensaje mensaje;

    public MessageItem(Mensaje mensaje) {
        this.mensaje = mensaje;
    }

    public Mensaje getMensaje() {
        return mensaje;
    }

    @Override
    public int getType() {
        return TYPE_MESSAGE;
    }
}
