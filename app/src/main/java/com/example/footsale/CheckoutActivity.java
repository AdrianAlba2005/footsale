package com.example.footsale;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footsale.adapter.CardSpinnerAdapter;
import com.example.footsale.adapter.CheckoutAdapter;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.TiendaApiService;
import com.example.footsale.api.models.CheckoutSummary;
import com.example.footsale.api.models.CheckoutSummaryRequest;
import com.example.footsale.api.models.ProcessCardOrderRequest;
import com.example.footsale.api.models.ProductQuantity;
import com.example.footsale.entidades.Card;
import com.example.footsale.utils.CartItem;
import com.example.footsale.utils.CartManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CheckoutAdapter adapter;
    private TextView tvSubtotal, tvShipping, tvVat, tvTotal;
    private Spinner cardSpinner;
    private Button confirmPurchaseButton;
    private List<CartItem> productList;
    private List<Card> cardList = new ArrayList<>();
    private CheckoutSummary currentSummary;
    private CardSpinnerAdapter cardSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initViews();
        productList = new ArrayList<>(CartManager.getInstance().getCartItems());
        
        setupRecyclerView();
        loadCards();
        loadCheckoutSummary();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.checkoutRecyclerView);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvShipping = findViewById(R.id.tvShipping);
        tvVat = findViewById(R.id.tvVat);
        tvTotal = findViewById(R.id.tvTotal);
        cardSpinner = findViewById(R.id.cardSpinner);
        confirmPurchaseButton = findViewById(R.id.confirmPurchaseButton);

        confirmPurchaseButton.setOnClickListener(v -> processOrder());
    }

    private void setupRecyclerView() {
        adapter = new CheckoutAdapter(this, productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadCards() {
        ApiClient.createUsuarioApiService(this).getMyCards().enqueue(new Callback<List<Card>>() {
            @Override
            public void onResponse(@NonNull Call<List<Card>> call, @NonNull Response<List<Card>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cardList = response.body();
                    setupCardSpinner();
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Card>> call, @NonNull Throwable t) {}
        });
    }

    private void setupCardSpinner() {
        cardSpinnerAdapter = new CardSpinnerAdapter(this, cardList);
        cardSpinner.setAdapter(cardSpinnerAdapter);
    }

    private void loadCheckoutSummary() {
        List<ProductQuantity> productQuantities = new ArrayList<>();
        for (CartItem item : productList) {
            productQuantities.add(new ProductQuantity(item.getProduct().getIdProducto(), item.getQuantity()));
        }

        CheckoutSummaryRequest request = new CheckoutSummaryRequest(productQuantities);
        ApiClient.createTiendaApiService(this).getCheckoutSummary(request).enqueue(new Callback<CheckoutSummary>() {
            @Override
            public void onResponse(@NonNull Call<CheckoutSummary> call, @NonNull Response<CheckoutSummary> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentSummary = response.body();
                    displaySummary(currentSummary);
                }
            }
            @Override
            public void onFailure(@NonNull Call<CheckoutSummary> call, @NonNull Throwable t) {}
        });
    }

    private void displaySummary(CheckoutSummary summary) {
        tvSubtotal.setText(String.format(Locale.GERMAN, "%.2f €", summary.getSubtotal()));
        tvShipping.setText(String.format(Locale.GERMAN, "%.2f €", summary.getShippingCost()));
        tvVat.setText(String.format(Locale.GERMAN, "%.2f €", summary.getVatAmount()));
        tvTotal.setText(String.format(Locale.GERMAN, "%.2f €", summary.getTotal()));
    }

    private void processOrder() {
        if (cardList.isEmpty() || currentSummary == null) {
            Toast.makeText(this, "Espera a que carguen todos los datos", Toast.LENGTH_SHORT).show();
            return;
        }

        List<ProductQuantity> productQuantities = new ArrayList<>();
        for (CartItem item : productList) {
            productQuantities.add(new ProductQuantity(item.getProduct().getIdProducto(), item.getQuantity()));
        }

        Card selectedCard = cardList.get(cardSpinner.getSelectedItemPosition());
        ProcessCardOrderRequest request = new ProcessCardOrderRequest(productQuantities, selectedCard.getId(), currentSummary);

        ApiClient.createTiendaApiService(this).processCardOrder(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CheckoutActivity.this, "Compra realizada con éxito", Toast.LENGTH_LONG).show();
                    CartManager.getInstance().clearCart();
                    
                    Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                    // IMPORTANTE: Estos flags limpian la pila de actividades para que al volver atrás no se pueda volver a la pantalla de pago
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); // Cerrar CheckoutActivity
                } else {
                    Toast.makeText(CheckoutActivity.this, "Error al procesar la compra", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(CheckoutActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
