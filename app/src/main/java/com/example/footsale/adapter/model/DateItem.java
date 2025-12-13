package com.example.footsale.adapter.model;

public class DateItem extends ChatItem {
    private String date;

    public DateItem(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int getType() {
        return TYPE_DATE;
    }
}
