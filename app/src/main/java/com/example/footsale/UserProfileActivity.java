package com.example.footsale;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.footsale.adapter.ProductoAdapter;
import com.example.footsale.adapter.ResenaAdapter;
import com.example.footsale.api.ApiClient;
import com.example.footsale.entidades.Producto;
import com.example.footsale.entidades.Resena;
import com.example.footsale.entidades.Usuario;
import com.example.footsale.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {

    public static final String EXTRA_USER_ID = "user_id";

    private ImageView profileAvatar;
    private TextView profileName;
    private RatingBar profileRatingBar;
    private RecyclerView userProductsRecyclerView;
    private RecyclerView reviewsRecyclerView;
    private Button addReviewButton;

    private ProductoAdapter productoAdapter;
    private ResenaAdapter resenaAdapter;
    private List<Producto> productList = new ArrayList<>();
    private List<Resena> resenaList = new ArrayList<>();
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userId = getIntent().getIntExtra(EXTRA_USER_ID, -1);
        if (userId == -1) {
            finish();
            return;
        }

        initViews();
        setupRecyclerViews();
        loadUserProfile();
        loadUserProducts();
        loadUserReviews();

        // Ocultar el botón de añadir reseña si el usuario se está viendo a sí mismo
        if (new SessionManager(this).getUserId() == userId) {
            addReviewButton.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        profileAvatar = findViewById(R.id.profileAvatar);
        profileName = findViewById(R.id.profileName);
        profileRatingBar = findViewById(R.id.profileRatingBar);
        userProductsRecyclerView = findViewById(R.id.userProductsRecyclerView);
        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        addReviewButton = findViewById(R.id.buttonAddReview);
    }

    private void setupRecyclerViews() {
        // RecyclerView de Productos
        productoAdapter = new ProductoAdapter(this, productList, new SessionManager(this).getUserId());
        userProductsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        userProductsRecyclerView.setAdapter(productoAdapter);

        // RecyclerView de Reseñas
        resenaAdapter = new ResenaAdapter(this, resenaList);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewsRecyclerView.setAdapter(resenaAdapter);
    }

    private void loadUserProfile() {
        ApiClient.createUsuarioApiService(this).getUserProfile(userId).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario user = response.body();
                    profileName.setText(user.getNombre());
                    if (user.getFotoPerfil() != null && !user.getFotoPerfil().isEmpty()) {
                        Glide.with(UserProfileActivity.this)
                            .load(ApiClient.getFullImageUrl(user.getFotoPerfil()))
                            .placeholder(R.drawable.ic_person)
                            .into(profileAvatar);
                    }
                    // Aquí se debería calcular y mostrar el rating promedio
                }
            }
            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {}
        });
    }

    private void loadUserProducts() {
        ApiClient.createTiendaApiService(this).getAllProducts(userId, 1).enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(@NonNull Call<List<Producto>> call, @NonNull Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body());
                    productoAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Producto>> call, @NonNull Throwable t) {}
        });
    }

    private void loadUserReviews() {
        ApiClient.createTiendaApiService(this).getUserReviews(userId).enqueue(new Callback<List<Resena>>() {
            @Override
            public void onResponse(@NonNull Call<List<Resena>> call, @NonNull Response<List<Resena>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resenaList.clear();
                    resenaList.addAll(response.body());
                    resenaAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Resena>> call, @NonNull Throwable t) {}
        });
    }
}
