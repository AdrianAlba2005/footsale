package com.example.footsale;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.footsale.api.ApiClient;
import com.example.footsale.api.models.AddCardRequest;
import com.google.android.material.textfield.TextInputEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCardActivity extends AppCompatActivity {

    private TextInputEditText cardNumber, expiryDate, cvv;
    private RadioGroup cardTypeGroup;
    private Button btnAddCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        
        setupToolbar();

        cardNumber = findViewById(R.id.card_number);
        expiryDate = findViewById(R.id.expiry_date);
        cvv = findViewById(R.id.cvv);
        cardTypeGroup = findViewById(R.id.radio_group_card_type);
        btnAddCard = findViewById(R.id.btn_add_card);

        setupCardNumberFormatting();
        setupExpiryDateFormatting();

        btnAddCard.setOnClickListener(v -> {
            String number = cardNumber.getText().toString().replace(" ", ""); // Quitar espacios
            String expiry = expiryDate.getText().toString();
            String cvvCode = cvv.getText().toString();
            
            if (number.length() != 16) {
                Toast.makeText(this, "Número de tarjeta inválido", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (expiry.length() != 5 || !expiry.contains("/")) {
                Toast.makeText(this, "Fecha inválida (MM/AA)", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Verificar tipo seleccionado
            int selectedId = cardTypeGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Selecciona el tipo de tarjeta", Toast.LENGTH_SHORT).show();
                return;
            }
            
            String cardType = (selectedId == R.id.rb_visa) ? "Visa" : "Mastercard";

            String[] parts = expiry.split("/");
            String month = parts[0];
            String year = parts[1];
            
            // Crear request con el nuevo parámetro 'type'
            AddCardRequest request = new AddCardRequest(number, month, year, cvvCode, cardType);
            
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

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupCardNumberFormatting() {
        cardNumber.addTextChangedListener(new TextWatcher() {
            private static final char SPACE_CHAR = ' ';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Eliminar el listener para evitar bucles infinitos
                cardNumber.removeTextChangedListener(this);

                // Obtener texto limpio sin espacios
                String initial = s.toString().replace(String.valueOf(SPACE_CHAR), "");
                StringBuilder sb = new StringBuilder();

                // Insertar espacio cada 4 caracteres
                for (int i = 0; i < initial.length(); i++) {
                    if (i > 0 && i % 4 == 0) {
                        sb.append(SPACE_CHAR);
                    }
                    sb.append(initial.charAt(i));
                }

                // Limitar a 19 caracteres (16 dígitos + 3 espacios)
                if (sb.length() > 19) {
                    sb.delete(19, sb.length());
                }

                cardNumber.setText(sb.toString());
                cardNumber.setSelection(cardNumber.getText().length()); // Mover cursor al final

                // Volver a añadir el listener
                cardNumber.addTextChangedListener(this);
            }
        });
    }

    private void setupExpiryDateFormatting() {
        expiryDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Si el usuario escribe el segundo dígito del mes, añadir barra automáticamente
                if (s.length() == 2 && start == 1 && count == 1 && !s.toString().contains("/")) {
                    expiryDate.setText(s + "/");
                    expiryDate.setSelection(expiryDate.getText().length());
                }
                // Si borra la barra, borrar también el dígito anterior para facilitar corrección
                else if (s.length() == 2 && start == 2 && count == 0) {
                   // Lógica opcional si se quiere borrar más agresivamente
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
