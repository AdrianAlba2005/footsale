package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class DeleteReviewRequest {
    @SerializedName("id_resena")
    private final int id_resena;

    public DeleteReviewRequest(int reviewId) {
        this.id_resena = reviewId;
    }
}
