package com.example.footsale;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.UsuarioApiService;
import com.example.footsale.api.models.ResetPasswordRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText editTextCode, editTextNewPassword, editTextConfirmPassword;
    private Button btnResetPassword;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        email = getIntent().getStringExtra("EMAIL");
        if (email == null) {
            Toast.makeText(this, "Error: Email no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        editTextCode = findViewById(R.id.editTextCode);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        btnResetPassword.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String code = editTextCode.getText().toString().trim();
        String newPassword = editTextNewPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(code) || TextUtils.isEmpty(newPassword)) {
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (code.length() != 6) {
            editTextCode.setError("El código debe tener 6 dígitos");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Las contraseñas no coinciden");
            return;
        }

        UsuarioApiService apiService = ApiClient.createUsuarioApiService(this);
        // Usamos el modelo ResetPasswordRequest que crearemos a continuación
        ResetPasswordRequest request = new ResetPasswordRequest(email, code, newPassword);

        apiService.resetPassword(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ResetPasswordActivity.this, "¡Contraseña restablecida con éxito!", Toast.LENGTH_LONG).show();
                    // Volver al login y limpiar pila
                    Intent intent = new Intent(ResetPasswordActivity.this, AuthActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Error: Código incorrecto o expirado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ResetPasswordActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
