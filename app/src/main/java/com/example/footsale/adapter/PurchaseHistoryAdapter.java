package com.example.footsale.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.footsale.PurchaseDetailActivity;
import com.example.footsale.R;
import com.example.footsale.entidades.Pedido;
import java.util.List;

public class PurchaseHistoryAdapter extends RecyclerView.Adapter<PurchaseHistoryAdapter.PurchaseViewHolder> {

    private List<Pedido> orderList;
    private Context context;

    public PurchaseHistoryAdapter(Context context, List<Pedido> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new PurchaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseViewHolder holder, int position) {
        Pedido pedido = orderList.get(position);
        
        holder.orderId.setText("Pedido #" + pedido.getIdPedido());
        holder.orderDate.setText(pedido.getFecha());
        holder.orderTotal.setText(String.format("Total: €%.2f", pedido.getTotal()));
        holder.orderStatus.setText(pedido.getEstado());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PurchaseDetailActivity.class);
            // Pasamos el objeto pedido completo, ya que ahora es Serializable
            intent.putExtra(PurchaseDetailActivity.EXTRA_PEDIDO, pedido);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class PurchaseViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderDate, orderTotal, orderStatus;

        public PurchaseViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderIdTextView);
            orderDate = itemView.findViewById(R.id.orderDateTextView);
            orderTotal = itemView.findViewById(R.id.orderTotalTextView);
            orderStatus = itemView.findViewById(R.id.orderStatusTextView);
        }
    }
}
