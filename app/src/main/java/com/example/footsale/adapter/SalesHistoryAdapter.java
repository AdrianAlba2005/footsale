package com.example.footsale.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.footsale.R;
import com.example.footsale.entidades.Venta;
import java.util.List;
import java.util.Locale;

public class SalesHistoryAdapter extends RecyclerView.Adapter<SalesHistoryAdapter.SaleViewHolder> {

    private List<Venta> salesList;

    public SalesHistoryAdapter(List<Venta> salesList) {
        this.salesList = salesList;
    }

    @NonNull
    @Override
    public SaleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sale_history, parent, false);
        return new SaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleViewHolder holder, int position) {
        Venta sale = salesList.get(position);
        holder.dateTextView.setText(sale.getFecha());
        holder.productNameTextView.setText(sale.getNombreProducto() + " (x" + sale.getCantidad() + ")");
        holder.buyerNameTextView.setText("Comprador: " + sale.getNombreComprador());
        holder.totalTextView.setText(String.format(Locale.GERMAN, "+ %.2f €", sale.getTotal()));
    }

    @Override
    public int getItemCount() {
        return salesList.size();
    }

    static class SaleViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, productNameTextView, buyerNameTextView, totalTextView;

        public SaleViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.saleDate);
            productNameTextView = itemView.findViewById(R.id.saleProductName);
            buyerNameTextView = itemView.findViewById(R.id.saleBuyerName);
            totalTextView = itemView.findViewById(R.id.saleTotal);
        }
    }
}
