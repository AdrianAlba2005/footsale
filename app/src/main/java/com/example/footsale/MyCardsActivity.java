package com.example.footsale;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.footsale.adapter.MyCardsAdapter;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.UsuarioApiService;
import com.example.footsale.entidades.Card;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCardsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private Button btnAddCardFromEmpty;
    private FloatingActionButton fabAddCard;
    private MyCardsAdapter adapter;
    private List<Card> cardList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cards);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Mis Tarjetas");
        }

        initViews();
        setupRecyclerView();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar tarjetas cada vez que volvemos (por si acabamos de añadir una)
        loadCards();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.cardsRecyclerView);
        emptyView = findViewById(R.id.empty_view);
        btnAddCardFromEmpty = findViewById(R.id.btnAddCardFromEmpty);
        fabAddCard = findViewById(R.id.fab_add_card);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyCardsAdapter(this, cardList);
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        View.OnClickListener addCardListener = v -> {
            startActivity(new Intent(MyCardsActivity.this, AddCardActivity.class));
        };
        btnAddCardFromEmpty.setOnClickListener(addCardListener);
        fabAddCard.setOnClickListener(addCardListener);
    }

    private void loadCards() {
        UsuarioApiService apiService = ApiClient.createUsuarioApiService(this);
        apiService.getMyCards().enqueue(new Callback<List<Card>>() {
            @Override
            public void onResponse(@NonNull Call<List<Card>> call, @NonNull Response<List<Card>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cardList.clear();
                    cardList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    updateUI();
                } else {
                    Toast.makeText(MyCardsActivity.this, "Error al cargar tarjetas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Card>> call, @NonNull Throwable t) {
                Toast.makeText(MyCardsActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        if (cardList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
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
