package com.example.footsale.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.footsale.R;
import com.example.footsale.adapter.CategoriaAdapter;
import com.example.footsale.adapter.ProductoAdapter;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.TiendaApiService;
import com.example.footsale.entidades.Categoria;
import com.example.footsale.entidades.Producto;
import com.example.footsale.utils.SessionManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TiendaFragment extends Fragment {

    private RecyclerView productsRecyclerView;
    private RecyclerView categoriesRecyclerView;
    private ProductoAdapter productoAdapter;
    private CategoriaAdapter categoriaAdapter;
    private List<Producto> productList = new ArrayList<>();
    private List<Categoria> categoryList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;
    private SessionManager sessionManager;
    private ShimmerFrameLayout shimmerFrameLayout;
    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;
    private int currentCategoryId = 0;
    private String currentQuery = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tienda, container, false);

        productsRecyclerView = view.findViewById(R.id.recyclerView);
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        searchView = view.findViewById(R.id.search_view);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        sessionManager = new SessionManager(getContext());

        setupProductRecyclerView();
        setupCategoryRecyclerView();
        setupSearchView();

        swipeRefreshLayout.setOnRefreshListener(this::resetAndLoadFirstPage);

        loadInitialData();

        return view;
    }

    private void setupProductRecyclerView() {
        productoAdapter = new ProductoAdapter(getContext(), productList, sessionManager.getUserId());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        productsRecyclerView.setLayoutManager(layoutManager);
        productsRecyclerView.setAdapter(productoAdapter);

        productsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        loadNextPage();
                    }
                }
            }
        });
    }

    private void setupCategoryRecyclerView() {
        categoriaAdapter = new CategoriaAdapter(getContext(), categoryList, categoryId -> {
            currentCategoryId = categoryId;
            currentQuery = null;
            searchView.setQuery("", false);
            resetAndLoadFirstPage();
        });
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoriesRecyclerView.setAdapter(categoriaAdapter);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchHandler.removeCallbacks(searchRunnable);
                currentQuery = query;
                currentCategoryId = 0;
                categoriaAdapter.clearSelection();
                resetAndLoadFirstPage();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchHandler.removeCallbacks(searchRunnable);
                searchRunnable = () -> {
                    currentQuery = newText;
                    currentCategoryId = 0;
                    categoriaAdapter.clearSelection();
                    resetAndLoadFirstPage();
                };
                searchHandler.postDelayed(searchRunnable, 400);
                return true;
            }
        });
    }

    private void loadInitialData() {
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);
        loadCategories();
        resetAndLoadFirstPage();
    }

    private void resetAndLoadFirstPage() {
        currentPage = 1;
        isLastPage = false;
        productList.clear();
        productoAdapter.notifyDataSetChanged();
        loadProducts(currentPage);
    }

    private void loadNextPage() {
        currentPage++;
        loadProducts(currentPage);
    }

    private void loadCategories() {
        if (getContext() == null) return;
        ApiClient.createTiendaApiService(getContext()).getCategories().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(@NonNull Call<List<Categoria>> call, @NonNull Response<List<Categoria>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();
                    categoryList.addAll(response.body());
                    categoriaAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Categoria>> call, @NonNull Throwable t) {}
        });
    }

    private void loadProducts(int page) {
        if (getContext() == null) return;
        isLoading = true;
        if(page == 1) {
            shimmerFrameLayout.startShimmer();
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        }

        TiendaApiService apiService = ApiClient.createTiendaApiService(getContext());
        int userId = sessionManager.getUserId() > 0 ? sessionManager.getUserId() : 0;
        Call<List<Producto>> call;

        if (currentQuery != null && !currentQuery.trim().isEmpty()) {
            call = apiService.searchProducts(currentQuery, userId, page);
        } else if (currentCategoryId > 0) {
            call = apiService.getProductsByCategory(currentCategoryId, userId, page);
        } else {
            call = apiService.getAllProducts(userId, page);
        }

        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(@NonNull Call<List<Producto>> call, @NonNull Response<List<Producto>> response) {
                if (isAdded()) {
                    if (shimmerFrameLayout.isShimmerStarted()) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                    }
                    swipeRefreshLayout.setRefreshing(false);

                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().isEmpty()) {
                            isLastPage = true;
                        } else {
                            int startPosition = productList.size();
                            productList.addAll(response.body());
                            productoAdapter.notifyItemRangeInserted(startPosition, response.body().size());
                        }
                    } else {
                        Toast.makeText(getContext(), "Error al cargar productos", Toast.LENGTH_SHORT).show();
                    }
                    isLoading = false;
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Producto>> call, @NonNull Throwable t) {
                if (isAdded()) {
                    if (shimmerFrameLayout.isShimmerStarted()) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                    }
                    swipeRefreshLayout.setRefreshing(false);
                    isLoading = false;
                    Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
