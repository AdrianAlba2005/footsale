package com.example.footsale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.footsale.R;
import com.example.footsale.entidades.Card;
import java.util.List;
import java.util.Locale;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private final List<Card> cardList;
    private int selectedPosition = -1;
    private final OnCardClickListener listener;

    public interface OnCardClickListener {
        void onCardClick(Card card);
    }

    public CardAdapter(List<Card> cardList, OnCardClickListener listener) {
        this.cardList = cardList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = cardList.get(position);
        holder.bind(card, position);
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public Card getSelectedCard() {
        if (selectedPosition != -1 && selectedPosition < cardList.size()) {
            return cardList.get(selectedPosition);
        }
        return null;
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView cardIcon;
        TextView cardNumber, cardExpiry, cardLimit;
        RadioButton radioButton;

        CardViewHolder(View itemView) {
            super(itemView);
            cardIcon = itemView.findViewById(R.id.card_icon);
            cardNumber = itemView.findViewById(R.id.card_number);
            cardExpiry = itemView.findViewById(R.id.card_expiry);
            cardLimit = itemView.findViewById(R.id.card_limit);
            radioButton = itemView.findViewById(R.id.card_radio_button);
        }

        void bind(final Card card, final int position) {
            cardNumber.setText(String.format("•••• %s", card.getUltimosCuatro()));
            cardExpiry.setText(String.format("Expira: %02d/%d", card.getMesExpiracion(), card.getAnoExpiracion()));
            cardLimit.setText(String.format(Locale.GERMAN, "Límite: %.2f €", card.getLimite()));

            // --- ¡LÓGICA DE LOS LOGOS! ---
            if ("Visa".equalsIgnoreCase(card.getTipo())) {
                cardIcon.setImageResource(R.drawable.visa);
            } else if ("Mastercard".equalsIgnoreCase(card.getTipo())) {
                cardIcon.setImageResource(R.drawable.mastercard);
            } else {
                cardIcon.setVisibility(View.INVISIBLE); // Ocultar si no es ninguna de las dos
            }

            boolean isCheckoutContext = (listener == null);
            radioButton.setVisibility(isCheckoutContext ? View.VISIBLE : View.GONE);

            if (isCheckoutContext) {
                radioButton.setChecked(position == selectedPosition);
                View.OnClickListener clickListener = v -> {
                    selectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                };
                itemView.setOnClickListener(clickListener);
                radioButton.setOnClickListener(clickListener);
            } else {
                itemView.setOnClickListener(v -> listener.onCardClick(card));
            }
        }
    }
}
