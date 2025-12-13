package com.example.footsale;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.models.VerifyCodeRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationActivity extends AppCompatActivity {

    private EditText codeEditText;
    private Button verifyButton;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        email = getIntent().getStringExtra("email");
        codeEditText = findViewById(R.id.codeEditText);
        verifyButton = findViewById(R.id.verifyButton);

        verifyButton.setOnClickListener(v -> {
            String code = codeEditText.getText().toString();
            ApiClient.createUsuarioApiService(this).verifyCode(new VerifyCodeRequest(email, code)).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(VerificationActivity.this, "Cuenta verificada", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(VerificationActivity.this, "Error al verificar", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(VerificationActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
