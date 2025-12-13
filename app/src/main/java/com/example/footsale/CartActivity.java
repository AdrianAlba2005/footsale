package com.example.footsale;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footsale.adapter.CartAdapter;
import com.example.footsale.utils.CartItem;
import com.example.footsale.utils.CartManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnItemDeletedListener {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItem> cartItems;
    private TextView tvTotal, emptyCartView;
    private Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Carrito de Compras");
            }
        }

        initViews();
        setupRecyclerView();

        checkoutButton.setOnClickListener(v -> {
            if (CartManager.getInstance().getCartItems().isEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // CORRECCIÓN CLAVE: Actualiza la vista cada vez que la actividad vuelve a ser visible.
        // Esto asegura que el carrito aparezca vacío después de una compra exitosa.
        updateCartView();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.cartRecyclerView);
        tvTotal = findViewById(R.id.totalTextView);
        emptyCartView = findViewById(R.id.emptyCartTextView);
        checkoutButton = findViewById(R.id.checkoutButton);
    }

    private void setupRecyclerView() {
        cartItems = new ArrayList<>(); // La lista se inicializa vacía
        adapter = new CartAdapter(cartItems, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void updateCartView() {
        // Cargar siempre los datos más recientes desde el CartManager
        cartItems.clear();
        cartItems.addAll(CartManager.getInstance().getCartItems());
        adapter.notifyDataSetChanged();

        // Actualiza la visibilidad de las vistas
        if (cartItems.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvTotal.setVisibility(View.GONE);
            checkoutButton.setVisibility(View.GONE);
            emptyCartView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvTotal.setVisibility(View.VISIBLE);
            checkoutButton.setVisibility(View.VISIBLE);
            emptyCartView.setVisibility(View.GONE);
            tvTotal.setText(String.format(Locale.GERMAN, "Total: %.2f €", CartManager.getInstance().getTotal()));
        }
    }

    @Override
    public void onItemDeleted(int productId) {
        CartManager.getInstance().removeFromCart(productId);
        updateCartView(); // Reutiliza el método centralizado para actualizar la vista
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
