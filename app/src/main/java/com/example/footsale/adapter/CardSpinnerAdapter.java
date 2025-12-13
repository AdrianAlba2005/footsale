package com.example.footsale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.footsale.R;
import com.example.footsale.entidades.Card;

import java.util.List;

public class CardSpinnerAdapter extends ArrayAdapter<Card> {

    public CardSpinnerAdapter(Context context, List<Card> cardList) {
        super(context, 0, cardList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item_with_card_logo, parent, false);
        }

        ImageView ivCardLogo = convertView.findViewById(R.id.ivCardLogo);
        TextView tvCardDetails = convertView.findViewById(R.id.tvCardDetails);

        Card card = getItem(position);

        if (card != null) {
            if ("visa".equalsIgnoreCase(card.getTipo())) {
                ivCardLogo.setImageResource(R.drawable.visa);
            } else if ("mastercard".equalsIgnoreCase(card.getTipo())) {
                ivCardLogo.setImageResource(R.drawable.mastercard);
            } else {
                ivCardLogo.setImageResource(R.drawable.ic_credit_card); // Icono genérico
            }
            tvCardDetails.setText(card.getTipo() + " terminada en " + card.getUltimosCuatro());
        }

        return convertView;
    }
}
