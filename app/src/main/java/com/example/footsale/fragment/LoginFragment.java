package com.example.footsale.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.footsale.MainActivity;
import com.example.footsale.R;
import com.example.footsale.RequestPasswordResetActivity;
import com.example.footsale.VerificationActivity;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.UsuarioApiService;
import com.example.footsale.api.models.LoginRequest;
import com.example.footsale.api.models.LoginResponse;
import com.example.footsale.utils.AuthNavigationListener;
import com.example.footsale.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private TextInputEditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textViewGoToRegister, forgotPasswordLink;
    private AuthNavigationListener navigationListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AuthNavigationListener) {
            navigationListener = (AuthNavigationListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // CORREGIDO: Usar los IDs correctos del layout
        editTextEmail = view.findViewById(R.id.email_edit_text);
        editTextPassword = view.findViewById(R.id.password_edit_text);
        buttonLogin = view.findViewById(R.id.login_button);
        textViewGoToRegister = view.findViewById(R.id.register_text_view);
        forgotPasswordLink = view.findViewById(R.id.forgot_password_text_view);

        buttonLogin.setOnClickListener(v -> loginUser());
        textViewGoToRegister.setOnClickListener(v -> {
            if (navigationListener != null) {
                navigationListener.navigateToRegister();
            }
        });
        
        forgotPasswordLink.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), RequestPasswordResetActivity.class));
        });

        return view;
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        UsuarioApiService apiService = ApiClient.createUsuarioApiService(getContext());
        LoginRequest loginRequest = new LoginRequest(email, password);

        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    SessionManager sessionManager = new SessionManager(getContext());
                    sessionManager.saveAuthToken(token);

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JsonObject errorObject = new Gson().fromJson(errorBody, JsonObject.class);
                        
                        if (errorObject.has("unverified") && errorObject.get("unverified").getAsBoolean()) {
                            Toast.makeText(getContext(), "Cuenta no verificada. Por favor, introduce el código.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity(), VerificationActivity.class);
                            intent.putExtra("EMAIL", email);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "Credenciales inválidas", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error desconocido", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
