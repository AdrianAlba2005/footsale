package com.example.footsale;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.models.AddCardRequest;
import com.google.android.material.textfield.TextInputEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCardActivity extends AppCompatActivity {

    private TextInputEditText cardNumber, expiryMonth, expiryYear, cvv;
    private Button btnAddCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        cardNumber = findViewById(R.id.card_number);
        expiryMonth = findViewById(R.id.expiry_month);
        expiryYear = findViewById(R.id.expiry_year);
        cvv = findViewById(R.id.cvv);
        btnAddCard = findViewById(R.id.btn_add_card);

        btnAddCard.setOnClickListener(v -> {
            String number = cardNumber.getText().toString();
            String month = expiryMonth.getText().toString();
            String year = expiryYear.getText().toString();
            String cvvCode = cvv.getText().toString();
            
            AddCardRequest request = new AddCardRequest(number, month, year, cvvCode);
            
            ApiClient.createUsuarioApiService(this).addCard(request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AddCardActivity.this, "Tarjeta añadida", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddCardActivity.this, "Error al añadir la tarjeta", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(AddCardActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
