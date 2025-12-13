package com.example.footsale;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footsale.adapter.WithdrawCardAdapter;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.UsuarioApiService;
import com.example.footsale.api.models.WithdrawRequest;
import com.example.footsale.entidades.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawActivity extends AppCompatActivity {

    public static final String EXTRA_CURRENT_BALANCE = "current_balance";

    private RecyclerView cardsRecyclerView;
    private EditText amountEditText;
    private Button withdrawButton, addCardButton;
    private LinearLayout noCardsView;
    private TextView currentBalanceLabel;
    private List<Card> cardList = new ArrayList<>();
    private WithdrawCardAdapter adapter;
    private double currentBalance = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Retirar Saldo");
        }

        currentBalance = getIntent().getDoubleExtra(EXTRA_CURRENT_BALANCE, 0.0);

        initViews();
        loadUserCards();
    }

    private void initViews() {
        cardsRecyclerView = findViewById(R.id.cardsRecyclerView);
        amountEditText = findViewById(R.id.amountEditText);
        withdrawButton = findViewById(R.id.withdrawButton);
        addCardButton = findViewById(R.id.addCardButton);
        noCardsView = findViewById(R.id.noCardsView);
        currentBalanceLabel = findViewById(R.id.currentBalanceLabel);

        currentBalanceLabel.setText(String.format(Locale.GERMAN, "Saldo disponible: %.2f €", currentBalance));

        withdrawButton.setOnClickListener(v -> performWithdrawal());
        addCardButton.setOnClickListener(v -> {
            startActivity(new Intent(this, AddCardActivity.class));
        });
    }

    private void loadUserCards() {
        ApiClient.createUsuarioApiService(this).getMyCards().enqueue(new Callback<List<Card>>() {
            @Override
            public void onResponse(@NonNull Call<List<Card>> call, @NonNull Response<List<Card>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    cardList.clear();
                    cardList.addAll(response.body());
                    setupRecyclerView();
                } else {
                    showNoCardsView();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Card>> call, @NonNull Throwable t) {
                showNoCardsView();
            }
        });
    }

    private void showNoCardsView() {
        cardsRecyclerView.setVisibility(View.GONE);
        amountEditText.setVisibility(View.GONE);
        withdrawButton.setVisibility(View.GONE);
        noCardsView.setVisibility(View.VISIBLE);
    }

    private void setupRecyclerView() {
        adapter = new WithdrawCardAdapter(this, cardList, card -> {
            // Lógica al seleccionar una tarjeta (si se necesitara en el futuro)
        });
        cardsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardsRecyclerView.setAdapter(adapter);
    }

    private void performWithdrawal() {
        String amountStr = amountEditText.getText().toString();
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Introduce un importe", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        if (amount <= 0) {
            Toast.makeText(this, "El importe debe ser mayor que cero", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amount > currentBalance) {
            Toast.makeText(this, "No puedes retirar más de tu saldo disponible", Toast.LENGTH_LONG).show();
            return;
        }

        Card selectedCard = adapter.getSelectedCard();
        if (selectedCard == null) {
            Toast.makeText(this, "Selecciona una tarjeta", Toast.LENGTH_SHORT).show();
            return;
        }

        WithdrawRequest request = new WithdrawRequest(selectedCard.getId(), amount);
        ApiClient.createUsuarioApiService(this).withdrawBalance(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(WithdrawActivity.this, "Retirada procesada con éxito", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(WithdrawActivity.this, "Error al retirar. Saldo insuficiente o error del servidor.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(WithdrawActivity.this, "Error de red al procesar la retirada", Toast.LENGTH_SHORT).show();
            }
        });
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
