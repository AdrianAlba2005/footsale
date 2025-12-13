package com.example.footsale.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.footsale.ProductDetailActivity;
import com.example.footsale.R;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.models.ToggleFavoriteRequest;
import com.example.footsale.entidades.Producto;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteProductAdapter extends RecyclerView.Adapter<FavoriteProductAdapter.ViewHolder> {

    private final Context context;
    private final List<Producto> favoriteList;

    public FavoriteProductAdapter(Context context, List<Producto> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = favoriteList.get(position);
        holder.productName.setText(producto.getTitulo());
        holder.productPrice.setText(String.format(Locale.GERMAN, "%.2f €", producto.getPrecio()));

        holder.favoriteIcon.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                favoriteList.remove(currentPosition);
                notifyItemRemoved(currentPosition);
                toggleFavorite(producto.getIdProducto());
            }
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, producto.getIdProducto());
            context.startActivity(intent);
        });
    }

    private void toggleFavorite(int productId) {
        ApiClient.createTiendaApiService(context).toggleFavorite(new ToggleFavoriteRequest(productId)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                // No es necesario hacer nada aquí, ya se ha actualizado la UI
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice;
        ImageView favoriteIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.tvProductName);
            productPrice = itemView.findViewById(R.id.tvProductPrice);
            favoriteIcon = itemView.findViewById(R.id.ivFavorite);
        }
    }
}
