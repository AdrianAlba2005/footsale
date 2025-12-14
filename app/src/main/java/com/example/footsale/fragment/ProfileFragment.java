package com.example.footsale.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.example.footsale.EditProfileActivity;
import com.example.footsale.PublishProductActivity;
import com.example.footsale.R;
import com.example.footsale.WithdrawActivity;
import com.example.footsale.adapter.ProfilePagerAdapter;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.TiendaApiService;
import com.example.footsale.api.UsuarioApiService;
import com.example.footsale.entidades.Resena;
import com.example.footsale.entidades.Usuario;
import com.example.footsale.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.List;
import java.util.Locale;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private CircleImageView profileAvatar;
    private TextView profileName, profileBalance;
    private RatingBar profileRatingBar;
    private Button withdrawButton;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FloatingActionButton fabAddProduct;
    private SessionManager sessionManager;
    private Usuario currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(getContext());

        profileAvatar = view.findViewById(R.id.profileAvatar);
        profileName = view.findViewById(R.id.profileName);
        profileRatingBar = view.findViewById(R.id.profileRatingBar);
        profileBalance = view.findViewById(R.id.profileBalance);
        withdrawButton = view.findViewById(R.id.withdrawButton);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        fabAddProduct = view.findViewById(R.id.fab_add_product);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Mi Perfil");
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.profile_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_edit_profile) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
                return true;
            }
            return false;
        });

        setupViewPager();
        
        fabAddProduct.setOnClickListener(v -> 
            startActivity(new Intent(getActivity(), PublishProductActivity.class)));
        
        withdrawButton.setOnClickListener(v -> {
            if (currentUser != null && currentUser.getSaldo() > 0) {
                Intent intent = new Intent(getActivity(), WithdrawActivity.class);
                intent.putExtra(WithdrawActivity.EXTRA_CURRENT_BALANCE, currentUser.getSaldo());
                startActivity(intent);
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                fabAddProduct.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProfileHeader();
        loadUserRating();
    }

    private void setupViewPager() {
        if (getContext() == null) return;
        ProfilePagerAdapter adapter = new ProfilePagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Mis Productos"); break;
                case 1: tab.setText("Favoritos"); break;
                case 2: tab.setText("Mis Reseñas"); break;
                case 3: tab.setText("Mis Compras"); break;
                case 4: tab.setText("Configuración"); break;
            }
        }).attach();
    }

    private void loadProfileHeader() {
        if (getContext() == null) return;
        ApiClient.createUsuarioApiService(getContext()).getProfileInfo().enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    currentUser = response.body();
                    profileName.setText(currentUser.getNombre());
                    profileBalance.setText(String.format(Locale.GERMAN, "%.2f €", currentUser.getSaldo()));
                    
                    // --- LÓGICA PARA DESACTIVAR EL BOTÓN ---
                    if (currentUser.getSaldo() > 0) {
                        withdrawButton.setEnabled(true);
                        withdrawButton.setAlpha(1.0f);
                    } else {
                        withdrawButton.setEnabled(false);
                        withdrawButton.setAlpha(0.5f);
                    }

                    if (currentUser.getFotoPerfil() != null && !currentUser.getFotoPerfil().isEmpty()) {
                        Glide.with(ProfileFragment.this).load(ApiClient.getFullImageUrl(currentUser.getFotoPerfil())).placeholder(R.drawable.ic_person).into(profileAvatar);
                    } else {
                        profileAvatar.setImageResource(R.drawable.ic_person);
                    }
                }
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {}
        });
    }

    private void loadUserRating() {
        if (getContext() == null || sessionManager == null) return;
        int userId = sessionManager.getUserId();
        if (userId == -1) return;

        ApiClient.createTiendaApiService(getContext()).getUserReviews(userId).enqueue(new Callback<List<Resena>>() {
            @Override
            public void onResponse(Call<List<Resena>> call, Response<List<Resena>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    calculateAndSetRating(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Resena>> call, Throwable t) {
                if(isAdded()) profileRatingBar.setRating(0);
            }
        });
    }

    private void calculateAndSetRating(List<Resena> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            profileRatingBar.setRating(0);
            return;
        }
        float totalScore = 0;
        for (Resena review : reviews) {
            totalScore += review.getPuntuacion();
        }
        float averageRating = totalScore / reviews.size();
        profileRatingBar.setRating(averageRating);
    }
}
