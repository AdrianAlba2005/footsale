package com.example.footsale.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.footsale.fragment.MyProductsFragment;
import com.example.footsale.fragment.MyReviewsFragment;
import com.example.footsale.fragment.MyFavoritesFragment;
import com.example.footsale.fragment.MyPurchasesFragment;
import com.example.footsale.fragment.SettingsFragment;

public class ProfilePagerAdapter extends FragmentStateAdapter {

    public ProfilePagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MyProductsFragment();
            case 1:
                return new MyFavoritesFragment();
            case 2:
                return new MyReviewsFragment();
            case 3:
                return new MyPurchasesFragment();
            case 4:
                return new SettingsFragment(); // Nueva pestaña
            default:
                return new MyProductsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5; // Ahora son 5 pestañas
    }
}
