package com.example.footsale.adapter;

import android.content.Context;
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
import java.util.Locale;

public class WithdrawCardAdapter extends RecyclerView.Adapter<WithdrawCardAdapter.ViewHolder> {

    private Context context;
    private List<Card> cardList;
    private int selectedPosition = 0; 

    public interface OnCardSelectedListener {
        void onCardSelected(Card card);
    }
    private OnCardSelectedListener listener;

    public WithdrawCardAdapter(Context context, List<Card> cardList, OnCardSelectedListener listener) {
        this.context = context;
        this.cardList = cardList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_withdraw_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card card = cardList.get(position);

        holder.cardType.setText(card.getTipo());
        holder.cardNumber.setText("**** **** **** " + card.getUltimosCuatro());
        holder.cardBalance.setText(String.format(Locale.GERMAN, "%.2f €", card.getLimite()));

        // CORREGIDO: Usar los drawables .png que ya existen
        if ("visa".equalsIgnoreCase(card.getTipo())) {
            holder.cardIcon.setImageResource(R.drawable.visa);
        } else if ("mastercard".equalsIgnoreCase(card.getTipo())) {
            holder.cardIcon.setImageResource(R.drawable.mastercard);
        } else {
            // Un icono genérico por si hay otros tipos de tarjeta
            holder.cardIcon.setImageResource(R.drawable.ic_credit_card); 
        }

        holder.itemView.setSelected(selectedPosition == position);

        holder.itemView.setOnClickListener(v -> {
            if (selectedPosition != holder.getAdapterPosition()) {
                int oldPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(oldPosition);
                notifyItemChanged(selectedPosition);
                listener.onCardSelected(cardList.get(selectedPosition));
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }
    
    public Card getSelectedCard() {
        if (cardList.isEmpty() || selectedPosition >= cardList.size()) {
            return null;
        }
        return cardList.get(selectedPosition);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cardIcon;
        TextView cardType, cardNumber, cardBalance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardIcon = itemView.findViewById(R.id.ivCardIcon);
            cardType = itemView.findViewById(R.id.tvCardType);
            cardNumber = itemView.findViewById(R.id.tvCardNumber);
            cardBalance = itemView.findViewById(R.id.tvCardBalance);
        }
    }
}
