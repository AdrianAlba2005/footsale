package com.example.footsale.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.footsale.R;
import com.example.footsale.entidades.Card;
import java.util.List;

public class MyCardsAdapter extends RecyclerView.Adapter<MyCardsAdapter.CardViewHolder> {

    private Context context;
    private List<Card> cardList;

    public MyCardsAdapter(Context context, List<Card> cardList) {
        this.context = context;
        this.cardList = cardList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = cardList.get(position);

        String cardType = card.getTipo() != null ? card.getTipo() : "Tarjeta";
        holder.cardType.setText(cardType);
        
        String last4 = card.getUltimosCuatro() != null ? card.getUltimosCuatro() : "0000";
        holder.cardNumber.setText("**** **** **** " + last4);

        // --- LÓGICA DE SALDO ---
        double saldo = card.getLimite(); // Asumiendo que 'limite' se usa como saldo disponible
        holder.cardBalance.setText(String.format("%.2f €", saldo));
        
        if (saldo > 0) {
            holder.cardBalance.setTextColor(Color.parseColor("#4CAF50")); // Verde
        } else {
            holder.cardBalance.setTextColor(Color.RED); // Rojo
        }

        // --- LÓGICA PARA ASIGNAR EL ICONO ---
        if (cardType.toLowerCase().contains("visa")) {
            holder.cardIcon.setImageResource(R.drawable.visa);
        } else if (cardType.toLowerCase().contains("mastercard")) { 
            holder.cardIcon.setImageResource(R.drawable.mastercard);
        } else {
            holder.cardIcon.setImageResource(R.drawable.ic_card); 
        }
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView cardIcon;
        TextView cardType, cardNumber, cardBalance; // Añadido cardBalance

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardIcon = itemView.findViewById(R.id.cardIcon);
            cardType = itemView.findViewById(R.id.cardType);
            cardNumber = itemView.findViewById(R.id.cardNumber);
            cardBalance = itemView.findViewById(R.id.cardBalance); // Vinculación
        }
    }
}
