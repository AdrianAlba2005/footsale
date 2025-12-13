package com.example.footsale.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.footsale.R;
import com.example.footsale.api.ApiClient;
import com.example.footsale.entidades.Categoria;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterBottomSheet extends BottomSheetDialogFragment {

    private LinearLayout categoryContainer;
    private Button applyButton;
    private List<Integer> selectedCategoryIds = new ArrayList<>();
    private FilterListener listener;

    public interface FilterListener {
        void onApplyFilters(List<Integer> categoryIds);
    }

    public static FilterBottomSheet newInstance(ArrayList<Integer> selectedIds) {
        FilterBottomSheet fragment = new FilterBottomSheet();
        Bundle args = new Bundle();
        args.putIntegerArrayList("selected_ids", selectedIds);
        fragment.setArguments(args);
        return fragment;
    }

    public void setFilterListener(FilterListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_bottom_sheet, container, false);

        categoryContainer = view.findViewById(R.id.categoryContainer);
        applyButton = view.findViewById(R.id.applyButton);

        if (getArguments() != null) {
            selectedCategoryIds = getArguments().getIntegerArrayList("selected_ids");
        }

        loadCategories();

        applyButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onApplyFilters(selectedCategoryIds);
            }
            dismiss();
        });

        return view;
    }

    private void loadCategories() {
        ApiClient.createTiendaApiService(getContext()).getCategories().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayCategories(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
            }
        });
    }

    private void displayCategories(List<Categoria> categories) {
        categoryContainer.removeAllViews();
        for (Categoria cat : categories) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(cat.getNombre());
            if (selectedCategoryIds.contains(cat.getId_categoria())) {
                checkBox.setChecked(true);
            }

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (!selectedCategoryIds.contains(cat.getId_categoria())) {
                        selectedCategoryIds.add(cat.getId_categoria());
                    }
                } else {
                    selectedCategoryIds.remove(Integer.valueOf(cat.getId_categoria()));
                }
            });
            categoryContainer.addView(checkBox);
        }
    }
}
