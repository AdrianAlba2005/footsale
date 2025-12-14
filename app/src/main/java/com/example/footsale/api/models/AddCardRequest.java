package com.example.footsale.api.models;

import com.google.gson.annotations.SerializedName;

public class AddCardRequest {
    @SerializedName("numero") // Ajustado para coincidir con el backend $d->numero
    private final String cardNumber;

    @SerializedName("mes_expiracion") // Ajustado para backend $d->mes_expiracion
    private final String expiryMonth;

    @SerializedName("ano_expiracion") // Ajustado para backend $d->ano_expiracion
    private final String expiryYear;

    @SerializedName("cvv") 
    private final String cvv;
    
    @SerializedName("tipo") // Nuevo campo requerido por el backend $d->tipo
    private final String type;

    public AddCardRequest(String cardNumber, String expiryMonth, String expiryYear, String cvv, String type) {
        this.cardNumber = cardNumber;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.cvv = cvv;
        this.type = type;
    }
}
