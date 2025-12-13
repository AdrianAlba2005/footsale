package com.example.footsale.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.footsale.ProductDetailActivity;
import com.example.footsale.R;
import com.example.footsale.entidades.Producto;
import java.util.List;
import java.util.Locale;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private final Context context;
    private final List<Producto> productoList;
    private final int currentUserId;

    public ProductoAdapter(Context context, List<Producto> productoList, int currentUserId) {
        this.context = context;
        this.productoList = productoList;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productoList.get(position);

        holder.tvTitulo.setText(producto.getTitulo());
        holder.tvPrecio.setText(String.format(Locale.GERMAN, "%.2f €", producto.getPrecio()));

        if (producto.getImagenPrincipal() != null && !producto.getImagenPrincipal().isEmpty()) {
            Glide.with(context).load(producto.getImagenPrincipal()).placeholder(R.drawable.ic_image_placeholder).into(holder.ivProducto);
        } else {
            holder.ivProducto.setImageResource(R.drawable.ic_image_placeholder);
        }
        
        // --- LÓGICA PARA MOSTRAR "AGOTADO" ---
        if (producto.getCantidad() <= 0) {
            holder.tvAgotado.setVisibility(View.VISIBLE);
            holder.itemView.setAlpha(0.7f); // Opcional: atenuar el item completo
        } else {
            holder.tvAgotado.setVisibility(View.GONE);
            holder.itemView.setAlpha(1.0f);
        }

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, producto.getIdProducto());

            Pair<View, String> p1 = Pair.create(holder.ivProducto, "product_image_" + producto.getIdProducto());
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, p1);
            context.startActivity(intent, options.toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProducto;
        TextView tvTitulo, tvPrecio, tvAgotado; // Añadido tvAgotado

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProducto = itemView.findViewById(R.id.ivProducto);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvAgotado = itemView.findViewById(R.id.tvAgotado); // Inicializar tvAgotado
        }
    }
}
