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
import com.example.footsale.adapter.PurchaseHistoryAdapter;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.TiendaApiService;
import com.example.footsale.entidades.Pedido;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPurchasesFragment extends Fragment {

    private RecyclerView recyclerView;
    private View emptyView;
    private PurchaseHistoryAdapter adapter;
    private List<Pedido> purchaseList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_purchases, container, false);

        recyclerView = view.findViewById(R.id.purchaseHistoryRecyclerView);
        emptyView = view.findViewById(R.id.emptyView);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PurchaseHistoryAdapter(getContext(), purchaseList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPurchaseHistory();
    }

    private void loadPurchaseHistory() {
        if (getContext() == null) return;
        TiendaApiService apiService = ApiClient.createTiendaApiService(getContext());
        apiService.getPurchaseHistory().enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    purchaseList.clear();
                    purchaseList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
                updateUI();
            }

            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                updateUI();
            }
        });
    }

    private void updateUI() {
        if (purchaseList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}
