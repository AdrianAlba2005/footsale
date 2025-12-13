package com.example.footsale;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footsale.adapter.PurchaseDetailAdapter;
import com.example.footsale.entidades.Pedido;

import java.util.Locale;

public class PurchaseDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PEDIDO = "extra_pedido";

    private TextView orderIdView, orderDateView, orderStatusView, orderTotalView;
    private RecyclerView productsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Detalle del Pedido");
        }

        initViews();

        Pedido pedido = (Pedido) getIntent().getSerializableExtra(EXTRA_PEDIDO);
        if (pedido != null) {
            displayOrderDetails(pedido);
        } else {
            Toast.makeText(this, "Error al cargar los detalles del pedido", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        orderIdView = findViewById(R.id.orderId);
        orderDateView = findViewById(R.id.orderDate);
        orderStatusView = findViewById(R.id.orderStatus);
        orderTotalView = findViewById(R.id.orderTotal);
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
    }

    private void displayOrderDetails(Pedido pedido) {
        orderIdView.setText("Pedido #" + pedido.getIdPedido());
        orderDateView.setText("Fecha: " + pedido.getFecha());
        orderStatusView.setText("Estado: " + pedido.getEstado());
        orderTotalView.setText(String.format(Locale.GERMAN, "Total: %.2f €", pedido.getTotal()));

        // Configurar RecyclerView para los productos
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        PurchaseDetailAdapter adapter = new PurchaseDetailAdapter(this, pedido.getDetalles());
        productsRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
