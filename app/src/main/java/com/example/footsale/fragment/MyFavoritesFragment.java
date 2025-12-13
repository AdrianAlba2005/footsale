package com.example.footsale.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class MyFavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private List<Producto> favoriteList = new ArrayList<>();
    private TextView emptyView;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_favorites, container, false);

        recyclerView = view.findViewById(R.id.favoritesRecyclerView);
        emptyView = view.findViewById(R.id.emptyView);
        sessionManager = new SessionManager(getContext());

        setupRecyclerView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavorites();
    }

    private void setupRecyclerView() {
        // Usar el ProductoAdapter que ya muestra las imágenes
        adapter = new ProductoAdapter(getContext(), favoriteList, sessionManager.getUserId());
        // Usar un GridLayout para mostrarlo en 2 columnas, como en la tienda
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
    }

    private void loadFavorites() {
        if (getContext() == null) return;

        ApiClient.createTiendaApiService(getContext()).getMyFavorites().enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(@NonNull Call<List<Producto>> call, @NonNull Response<List<Producto>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    favoriteList.clear();
                    favoriteList.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    // Mostrar u ocultar el mensaje de "vacío"
                    if (favoriteList.isEmpty()) {
                        emptyView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Producto>> call, @NonNull Throwable t) {
                if (isAdded()) {
                    Toast.makeText(getContext(), "Error al cargar favoritos", Toast.LENGTH_SHORT).show();
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }
}
