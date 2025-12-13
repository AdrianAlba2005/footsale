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
import com.example.footsale.adapter.UserAdapter;
import com.example.footsale.api.ApiClient;
import com.example.footsale.entidades.Usuario;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreAdminFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<Usuario> userList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_admin, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        setupRecyclerView();
        loadUsers();
        return view;
    }

    private void setupRecyclerView() {
        adapter = new UserAdapter(getContext(), userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadUsers() {
        ApiClient.createUsuarioApiService(getContext()).getAllUsers().enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userList.clear();
                    userList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {}
        });
    }
}
