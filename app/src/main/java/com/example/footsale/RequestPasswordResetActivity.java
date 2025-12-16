package com.example.footsale;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.models.EmailRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestPasswordResetActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_password_reset);

        emailEditText = findViewById(R.id.emailEditText);
        sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            ApiClient.createUsuarioApiService(this).requestPasswordReset(new EmailRequest(email)).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Intent intent = new Intent(RequestPasswordResetActivity.this, ResetPasswordActivity.class);
                        // Asegurar que la clave coincide exactamente con lo que espera la siguiente actividad
                        intent.putExtra("EMAIL", email);
                        startActivity(intent);
                        finish(); // Cerrar esta pantalla para que el usuario no vuelva aquí al pulsar atrás
                    } else {
                        Toast.makeText(RequestPasswordResetActivity.this, "Error al solicitar el código", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(RequestPasswordResetActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
