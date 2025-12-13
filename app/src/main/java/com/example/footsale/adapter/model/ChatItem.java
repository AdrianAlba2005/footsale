package com.example.footsale.adapter.model;

public abstract class ChatItem {
    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_DATE = 1;

    abstract public int getType();
}
