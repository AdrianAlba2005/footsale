package com.example.footsale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.footsale.R;
import com.example.footsale.entidades.Producto;
import com.example.footsale.utils.CartItem;
import java.util.List;
import java.util.Locale;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

    private final Context context;
    private final List<CartItem> productList;

    public CheckoutAdapter(Context context, List<CartItem> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = productList.get(position);
        Producto product = item.getProduct();
        holder.productName.setText(product.getTitulo());
        String details = String.format(Locale.GERMAN, "%d x %.2f €", item.getQuantity(), product.getPrecio());
        holder.productDetails.setText(details);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.tvProductName);
            productDetails = itemView.findViewById(R.id.tvProductDetails);
        }
    }
}
