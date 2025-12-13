package com.example.footsale;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.footsale.adapter.ProductoAdapter;
import com.example.footsale.api.ApiClient;
import com.example.footsale.entidades.Producto;
import com.example.footsale.utils.SessionManager;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private List<Producto> productList = new ArrayList<>();
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_products);

        recyclerView = findViewById(R.id.recyclerView);
        sessionManager = new SessionManager(this);

        setupRecyclerView();
        loadProducts();
    }

    private void setupRecyclerView() {
        adapter = new ProductoAdapter(this, productList, sessionManager.getUserId());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }

    private void loadProducts() {
        // CORREGIDO: Añadido el parámetro de la página
        ApiClient.createTiendaApiService(this).getAllProducts(0, 1).enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                // Manejar error
            }
        });
    }
}
