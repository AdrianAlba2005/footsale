package com.example.footsale.entidades;

import com.google.gson.annotations.SerializedName;

public class Card {

    @SerializedName("id_tarjeta")
    private int id;

    @SerializedName("tipo")
    private String tipo;

    @SerializedName("ultimos_cuatro")
    private String ultimosCuatro;

    @SerializedName("mes_expiracion")
    private int mesExpiracion;

    @SerializedName("ano_expiracion")
    private int anoExpiracion;

    @SerializedName("limite")
    private double limite; // <-- CAMPO NUEVO

    public int getId() { return id; }
    public String getTipo() { return tipo; }
    public String getUltimosCuatro() { return ultimosCuatro; }
    public int getMesExpiracion() { return mesExpiracion; }
    public int getAnoExpiracion() { return anoExpiracion; }
    public double getLimite() { return limite; } // <-- GETTER NUEVO
}
