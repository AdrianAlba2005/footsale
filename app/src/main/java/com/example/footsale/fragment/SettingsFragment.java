package com.example.footsale.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.footsale.AuthActivity;
import com.example.footsale.MyCardsActivity;
import com.example.footsale.R;
import com.example.footsale.SalesHistoryActivity;
import com.example.footsale.api.ApiClient;
import com.example.footsale.utils.SessionManager;

public class SettingsFragment extends Fragment {

    private Button btnMyCards, btnSalesHistory, btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        btnMyCards = view.findViewById(R.id.btnMyCards);
        btnSalesHistory = view.findViewById(R.id.btnSalesHistory);
        btnLogout = view.findViewById(R.id.btnLogout);

        setupListeners();

        return view;
    }

    private void setupListeners() {
        btnMyCards.setOnClickListener(v -> {
            // Comprobación de seguridad
            Activity activity = getActivity();
            if (activity != null) {
                startActivity(new Intent(activity, MyCardsActivity.class));
            }
        });
        
        btnSalesHistory.setOnClickListener(v -> {
            Activity activity = getActivity();
            if (activity != null) {
                startActivity(new Intent(activity, SalesHistoryActivity.class));
            }
        });
        
        btnLogout.setOnClickListener(v -> logout());
    }

    private void logout() {
        Activity activity = getActivity();
        if (activity == null) return;
        
        SessionManager sessionManager = new SessionManager(activity);
        sessionManager.clearSession();
        ApiClient.resetClient();

        Intent intent = new Intent(activity, AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
