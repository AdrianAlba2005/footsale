package com.example.footsale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.footsale.R;
import com.example.footsale.api.ApiClient;
import com.example.footsale.entidades.DetallePedido;
import java.util.List;
import java.util.Locale;

public class PurchaseDetailAdapter extends RecyclerView.Adapter<PurchaseDetailAdapter.DetailViewHolder> {

    private List<DetallePedido> detalles;
    private Context context;

    public PurchaseDetailAdapter(Context context, List<DetallePedido> detalles) {
        this.context = context;
        this.detalles = detalles;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_purchase_detail_product, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        DetallePedido detalle = detalles.get(position);

        holder.productName.setText(detalle.getTitulo());
        holder.productQuantity.setText("Cantidad: " + detalle.getCantidad());
        holder.productPrice.setText(String.format(Locale.GERMAN, "%.2f €", detalle.getPrecioUnitario()));

        // Cargar imagen con Glide
        Glide.with(context)
                .load(ApiClient.getFullImageUrl(detalle.getImagen())) 
                .placeholder(R.drawable.ic_image_placeholder)
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return detalles.size();
    }

    static class DetailViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productQuantity, productPrice;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productPrice = itemView.findViewById(R.id.productPrice);
        }
    }
}
