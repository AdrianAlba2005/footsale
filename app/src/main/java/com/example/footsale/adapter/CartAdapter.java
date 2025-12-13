package com.example.footsale.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footsale.R;
import com.example.footsale.utils.CartItem;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnItemDeletedListener {
        void onItemDeleted(int productId);
    }

    private final List<CartItem> cartItems;
    private final OnItemDeletedListener listener;

    public CartAdapter(List<CartItem> cartItems, OnItemDeletedListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(cartItems.get(position));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity;
        ImageButton deleteButton;

        CartViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }

        void bind(final CartItem cartItem) {
            // CORREGIDO: Usar getProduct() en lugar de getProducto()
            productName.setText(cartItem.getProduct().getTitulo());
            productPrice.setText(String.format(Locale.GERMAN, "%.2f €", cartItem.getProduct().getPrecio()));
            productQuantity.setText(String.format("Cantidad: %d", cartItem.getQuantity()));

            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemDeleted(cartItem.getProduct().getIdProducto());
                }
            });
        }
    }
}
