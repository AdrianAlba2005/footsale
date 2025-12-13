package com.example.footsale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.footsale.R;
import com.example.footsale.entidades.Categoria;
import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {

    private final Context context;
    private final List<Categoria> categorias;
    private final OnCategoryClickListener listener;
    private int selectedPosition = -1;

    public interface OnCategoryClickListener {
        void onCategoryClick(int categoryId);
    }

    public CategoriaAdapter(Context context, List<Categoria> categorias, OnCategoryClickListener listener) {
        this.context = context;
        this.categorias = categorias;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_categoria, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categoria categoria = categorias.get(position);
        holder.nombre.setText(categoria.getNombre());

        holder.itemView.setSelected(selectedPosition == position);

        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);
            // CORREGIDO: Usar el getter correcto de la entidad Categoria
            listener.onCategoryClick(categoria.getId_categoria());
        });
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public void clearSelection() {
        int previousSelected = selectedPosition;
        selectedPosition = -1;
        if (previousSelected != -1) {
            notifyItemChanged(previousSelected);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
