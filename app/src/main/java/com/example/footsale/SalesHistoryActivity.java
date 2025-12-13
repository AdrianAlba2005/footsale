package com.example.footsale;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.footsale.adapter.SalesHistoryAdapter;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.TiendaApiService;
import com.example.footsale.entidades.Venta;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private View emptyView; // Vista para cuando no hay ventas
    private SalesHistoryAdapter adapter;
    private List<Venta> salesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_history);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Mis Ventas");
        }

        initViews();
        loadSalesHistory();
    }

    private void initViews() {
        // CORREGIDO: Usar los IDs que realmente están en el XML
        recyclerView = findViewById(R.id.salesRecyclerView);
        emptyView = findViewById(R.id.emptyView);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SalesHistoryAdapter(salesList);
        recyclerView.setAdapter(adapter);
    }

    private void loadSalesHistory() {
        TiendaApiService apiService = ApiClient.createTiendaApiService(this);
        
        apiService.getSalesHistory().enqueue(new Callback<List<Venta>>() {
            @Override
            public void onResponse(@NonNull Call<List<Venta>> call, @NonNull Response<List<Venta>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    salesList.clear();
                    salesList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    
                    // Comprobar si la lista está vacía
                    if (salesList.isEmpty()) {
                        emptyView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(SalesHistoryActivity.this, "Error al cargar ventas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Venta>> call, @NonNull Throwable t) {
                Toast.makeText(SalesHistoryActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
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
