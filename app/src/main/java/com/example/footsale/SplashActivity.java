package com.example.footsale;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import com.example.footsale.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Instalar el Splash Screen ANTES de llamar a super.onCreate
        SplashScreen.installSplashScreen(this);
        
        super.onCreate(savedInstanceState);

        // No se necesita layout (setContentView) porque el tema ya provee la UI

        // La lógica de decisión se ejecuta inmediatamente
        SessionManager sessionManager = new SessionManager(this);

        Intent intent;
        if (sessionManager.isLoggedIn()) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, AuthActivity.class);
        }
        
        startActivity(intent);
        finish(); // Finalizar esta actividad
    }
}
