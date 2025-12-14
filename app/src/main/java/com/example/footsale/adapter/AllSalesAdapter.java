package com.example.footsale.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.footsale.R;
import com.example.footsale.entidades.DetallePedido;
import com.example.footsale.entidades.Pedido;
import java.util.List;

public class AllSalesAdapter extends RecyclerView.Adapter<AllSalesAdapter.ViewHolder> {

    private Context context;
    private List<Pedido> pedidos;

    public AllSalesAdapter(Context context, List<Pedido> pedidos) {
        this.context = context;
        this.pedidos = pedidos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sale_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);
        
        holder.txtIdPedido.setText("Pedido #" + pedido.getIdPedido());
        holder.txtFecha.setText(pedido.getFecha());
        holder.txtTotal.setText(String.format("%.2f €", pedido.getTotal()));
        holder.txtEstado.setText(pedido.getEstado());

        if (pedido.getUsuario() != null) {
            holder.txtUsuario.setText("Cliente: " + pedido.getUsuario().getNombre());
        } else {
            holder.txtUsuario.setText("Cliente: Desconocido");
        }

        // Limpiar el contenedor de productos reciclado
        holder.productsContainer.removeAllViews();

        // Si la lista de detalles no es nula, agregar TextViews dinámicamente
        if (pedido.getDetalles() != null && !pedido.getDetalles().isEmpty()) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                TextView productLine = new TextView(context);
                
                // Formato: "1x Zapatillas Nike (Talla 42)" 
                // Ajusta según los campos que tengas en DetallePedido. 
                // En tu BD tienes: id_detalle, id_pedido, id_producto, cantidad, precio_unitario.
                // Y tu clase DetallePedido tiene: titulo, imagen, cantidad, precioUnitario.
                
                String nombreProducto = (detalle.getTitulo() != null) ? detalle.getTitulo() : "Producto #" + detalle.getIdProducto();
                
                productLine.setText(detalle.getCantidad() + "x " + nombreProducto);
                productLine.setTextSize(14);
                productLine.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                productLine.setPadding(0, 2, 0, 2);
                
                holder.productsContainer.addView(productLine);
            }
        } else {
            // Opcional: Mostrar mensaje si no hay detalles cargados
            TextView emptyMsg = new TextView(context);
            emptyMsg.setText("Sin detalles");
            emptyMsg.setTextSize(12);
            emptyMsg.setTypeface(null, Typeface.ITALIC);
            holder.productsContainer.addView(emptyMsg);
        }
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtIdPedido, txtFecha, txtTotal, txtEstado, txtUsuario;
        LinearLayout productsContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIdPedido = itemView.findViewById(R.id.txtIdPedido);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtEstado = itemView.findViewById(R.id.txtEstado);
            txtUsuario = itemView.findViewById(R.id.txtUsuario);
            productsContainer = itemView.findViewById(R.id.productsContainer);
        }
    }
}
