package com.example.footsale.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.footsale.R;
import com.example.footsale.adapter.ProductoAdapter;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.TiendaApiService;
import com.example.footsale.entidades.Producto;
import com.example.footsale.utils.SessionManager;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private View emptyView;
    private ProductoAdapter adapter;
    private List<Producto> productList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_products, container, false);

        recyclerView = view.findViewById(R.id.myProductsRecyclerView);
        emptyView = view.findViewById(R.id.emptyView);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        SessionManager sessionManager = new SessionManager(getContext());
        adapter = new ProductoAdapter(getContext(), productList, sessionManager.getUserId());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMyProducts();
    }

    private void loadMyProducts() {
        if (getContext() == null) return;
        TiendaApiService apiService = ApiClient.createTiendaApiService(getContext());
        apiService.getMyProducts().enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
                updateUI();
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                updateUI();
            }
        });
    }
    
    private void updateUI() {
        if (productList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}
