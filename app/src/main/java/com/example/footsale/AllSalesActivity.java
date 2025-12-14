package com.example.footsale;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.footsale.adapter.AllSalesAdapter;
import com.example.footsale.api.ApiClient;
import com.example.footsale.entidades.Pedido;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllSalesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AllSalesAdapter adapter;
    private List<Pedido> salesList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_sales);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Todas las Ventas");
        }

        recyclerView = findViewById(R.id.recyclerViewAllSales);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AllSalesAdapter(this, salesList);
        recyclerView.setAdapter(adapter);

        loadAllSales();
    }

    private void loadAllSales() {
        ApiClient.createUsuarioApiService(this).getAllOrders().enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    salesList.clear();
                    salesList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                Toast.makeText(AllSalesActivity.this, "Error al cargar pedidos: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
