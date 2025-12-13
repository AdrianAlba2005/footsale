package com.example.footsale.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.footsale.R;
import com.example.footsale.VerificationActivity;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.UsuarioApiService;
import com.example.footsale.api.models.RegisterRequest;
import com.example.footsale.utils.AuthNavigationListener;
import com.google.android.material.textfield.TextInputEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private TextInputEditText editTextName, editTextEmail, editTextPassword;
    private Button buttonRegister;
    private TextView loginLink;
    private ProgressDialog progressDialog;
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        editTextName = view.findViewById(R.id.register_username);
        editTextEmail = view.findViewById(R.id.register_email);
        editTextPassword = view.findViewById(R.id.register_password);
        buttonRegister = view.findViewById(R.id.register_button);
        loginLink = view.findViewById(R.id.login_text_view);

        buttonRegister.setOnClickListener(v -> registerUser());
        
        loginLink.setOnClickListener(v -> {
            if (navigationListener != null) {
                navigationListener.navigateToLogin();
            }
        });

        return view;
    }

    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Enviando correo de verificación...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RegisterRequest registerRequest = new RegisterRequest(name, email, password);
        UsuarioApiService apiService = ApiClient.createUsuarioApiService(getContext());

        // CORREGIDO: Cambiado a Callback<Void>
        apiService.register(registerRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Registro exitoso, por favor verifica tu email.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), VerificationActivity.class);
                    intent.putExtra("EMAIL", email); // Pasar email a la siguiente actividad
                    startActivity(intent);
                } else {
                    // Lógica para manejar errores como email duplicado, etc.
                    Toast.makeText(getContext(), "Error en el registro. El email podría ya estar en uso.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
