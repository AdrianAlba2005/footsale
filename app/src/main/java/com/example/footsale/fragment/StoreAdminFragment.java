package com.example.footsale.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.footsale.AuthActivity;
import com.example.footsale.EditProfileActivity; // Importar EditProfileActivity
import com.example.footsale.R;
import com.example.footsale.AllSalesActivity;
import com.example.footsale.adapter.UserAdapter;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.models.DashboardStats;
import com.example.footsale.entidades.Usuario;
import com.example.footsale.utils.SessionManager;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreAdminFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<Usuario> userList = new ArrayList<>();

    private TextView statTotalUsers;
    private TextView statTotalProducts;
    private TextView statTotalOrders;
    private TextView statTotalMoney;
    private MaterialCardView cardTotalOrders;
    private ImageButton btnLogoutStore;
    private ImageButton btnEditProfile; // Nuevo botón

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_admin, container, false);
        
        // Referenciar TextViews de estadísticas
        statTotalUsers = view.findViewById(R.id.statTotalUsers);
        statTotalProducts = view.findViewById(R.id.statTotalProducts);
        statTotalOrders = view.findViewById(R.id.statTotalOrders);
        statTotalMoney = view.findViewById(R.id.statTotalMoney);
        btnLogoutStore = view.findViewById(R.id.btnLogoutStore);
        btnEditProfile = view.findViewById(R.id.btnEditProfile); // Referenciar el botón de editar perfil

        // Lógica de cerrar sesión
        btnLogoutStore.setOnClickListener(v -> {
            // Borrar sesión
            SessionManager sessionManager = new SessionManager(getContext());
            sessionManager.clearSession();

            // Redirigir a pantalla de login
            Intent intent = new Intent(getContext(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Lógica para ir a editar perfil
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        // Referenciar la tarjeta contenedora de "Ventas Totales" para hacerla clickeable
        View parentView = (View) statTotalOrders.getParent(); // LinearLayout
        if (parentView != null && parentView.getParent() instanceof MaterialCardView) {
            cardTotalOrders = (MaterialCardView) parentView.getParent();
            cardTotalOrders.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), AllSalesActivity.class);
                startActivity(intent);
            });
        }

        recyclerView = view.findViewById(R.id.usersRecyclerView);
        
        setupRecyclerView();
        loadStatistics();
        loadUsers();
        
        return view;
    }

    private void setupRecyclerView() {
        adapter = new UserAdapter(getContext(), userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadStatistics() {
        ApiClient.createUsuarioApiService(getContext()).getStatistics().enqueue(new Callback<DashboardStats>() {
            @Override
            public void onResponse(Call<DashboardStats> call, Response<DashboardStats> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DashboardStats stats = response.body();
                    statTotalUsers.setText(String.valueOf(stats.getTotalUsers()));
                    statTotalProducts.setText(String.valueOf(stats.getTotalProducts()));
                    statTotalOrders.setText(String.valueOf(stats.getTotalOrders()));
                    // Muestra el total de ventas con formato de moneda
                    statTotalMoney.setText(String.format("%.2f €", stats.getTotalSalesValue()));
                }
            }

            @Override
            public void onFailure(Call<DashboardStats> call, Throwable t) {
                // Opcional: Manejar error de carga
            }
        });
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
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(getContext(), "Error cargando usuarios", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
