package com.example.footsale;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.footsale.adapter.AuthPagerAdapter;
import com.example.footsale.utils.AuthNavigationListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AuthActivity extends AppCompatActivity implements AuthNavigationListener {

    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // CORREGIDO: Usar los IDs correctos del layout
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        AuthPagerAdapter adapter = new AuthPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Iniciar Sesión");
            } else {
                tab.setText("Registrarse");
            }
        }).attach();
    }

    @Override
    public void navigateToRegister() {
        viewPager.setCurrentItem(1); // Cambia a la pestaña de Registro (posición 1)
    }

    @Override
    public void navigateToLogin() {
        viewPager.setCurrentItem(0); // Cambia a la pestaña de Login (posición 0)
    }
}
