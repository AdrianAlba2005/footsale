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
import com.example.footsale.adapter.ResenaAdapter;
import com.example.footsale.api.ApiClient;
import com.example.footsale.entidades.Resena;
import com.example.footsale.utils.SessionManager;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyReviewsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ResenaAdapter adapter;
    private List<Resena> resenaList = new ArrayList<>();
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_reviews, container, false);
        recyclerView = view.findViewById(R.id.reviewsRecyclerView);
        sessionManager = new SessionManager(getContext());
        setupRecyclerView();
        loadReviews();
        return view;
    }

    private void setupRecyclerView() {
        adapter = new ResenaAdapter(getContext(), resenaList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadReviews() {
        int userId = sessionManager.getUserId();
        if (userId == -1) return;

        ApiClient.createTiendaApiService(getContext()).getUserReviews(userId).enqueue(new Callback<List<Resena>>() {
            @Override
            public void onResponse(@NonNull Call<List<Resena>> call, @NonNull Response<List<Resena>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    resenaList.clear();
                    resenaList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Resena>> call, @NonNull Throwable t) {}
        });
    }
}
