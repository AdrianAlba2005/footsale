package com.example.footsale.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.footsale.fragment.LoginFragment;
import com.example.footsale.fragment.RegisterFragment;

public class AuthPagerAdapter extends FragmentStateAdapter {

    public AuthPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new RegisterFragment();
        }
        return new LoginFragment();
    }

    @Override
    public int getItemCount() {
        return 2; // Tenemos dos pestañas: Iniciar Sesión y Registrarse
    }
}