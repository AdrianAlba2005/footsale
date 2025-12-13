package com.example.footsale;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class  AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // No es necesario un Toolbar en el layout, usaremos el del tema
        // y activaremos el botón de "atrás"
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Acerca de");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Maneja el clic en el botón de "atrás" de la barra de herramientas
        onBackPressed();
        return true;
    }
}