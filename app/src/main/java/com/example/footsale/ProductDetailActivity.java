package com.example.footsale;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.footsale.adapter.ImageSliderAdapter;
import com.example.footsale.adapter.ResenaAdapter;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.TiendaApiService;
import com.example.footsale.api.models.ReviewRequest;
import com.example.footsale.api.models.ToggleFavoriteRequest;
import com.example.footsale.entidades.Producto;
import com.example.footsale.entidades.Resena;
import com.example.footsale.utils.CartManager;
import com.example.footsale.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "extra_product_id";
    public static final String EXTRA_IS_READ_ONLY = "extra_is_read_only";

    private TextView productNameView, productPriceView, productStatusView, productDescriptionView, sellerNameView, quantityTextView, noReviewsText, imageCounter;
    private ViewPager2 imageSlider;
    private ImageButton arrowLeft, arrowRight;
    private MaterialButton decreaseButton, increaseButton, addToCartButton, addToFavoritesButton;
    private Button sendMessageButton, modifyProductButton, addReviewButton;
    private LinearLayout buyerView;
    private RecyclerView reviewsRecyclerView;

    private ResenaAdapter reviewAdapter;
    private List<Resena> reviewList = new ArrayList<>();
    private Producto currentProduct;
    private SessionManager sessionManager;
    private boolean isReadOnly = false;
    private int currentQuantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportPostponeEnterTransition();
        setContentView(R.layout.activity_product_detail);

        initToolbar();
        sessionManager = new SessionManager(this);
        initViews();
        setupListeners();

        isReadOnly = getIntent().getBooleanExtra(EXTRA_IS_READ_ONLY, false);
        int productId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, -1);

        if (productId != -1) {
            ViewCompat.setTransitionName(findViewById(R.id.imageSlider), "product_image_" + productId);
            loadProductDetails(productId);
            loadReviews(productId);
        } else {
            Toast.makeText(this, "Error: ID de producto inválido", Toast.LENGTH_SHORT).show();
            supportFinishAfterTransition();
        }
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    private void initViews() {
        productNameView = findViewById(R.id.product_name);
        productPriceView = findViewById(R.id.product_price);
        productStatusView = findViewById(R.id.product_status);
        productDescriptionView = findViewById(R.id.product_description);
        sellerNameView = findViewById(R.id.seller_name);
        buyerView = findViewById(R.id.buyer_view);
        modifyProductButton = findViewById(R.id.buttonModifyProduct);
        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        noReviewsText = findViewById(R.id.noReviewsText);
        addReviewButton = findViewById(R.id.buttonAddReview);
        imageSlider = findViewById(R.id.imageSlider);
        arrowLeft = findViewById(R.id.arrow_left);
        arrowRight = findViewById(R.id.arrow_right);
        imageCounter = findViewById(R.id.image_counter);
        quantityTextView = findViewById(R.id.quantity_text_view);
        decreaseButton = findViewById(R.id.button_decrease_quantity);
        increaseButton = findViewById(R.id.button_increase_quantity);
        addToCartButton = findViewById(R.id.buttonAddToCart);
        addToFavoritesButton = findViewById(R.id.buttonAddToFavorites);
        sendMessageButton = findViewById(R.id.buttonSendMessage);

        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ResenaAdapter(this, reviewList);
        reviewsRecyclerView.setAdapter(reviewAdapter);
    }

    private void setupListeners() {
        modifyProductButton.setOnClickListener(v -> openEditProduct());
        addReviewButton.setOnClickListener(v -> showAddReviewDialog());
        if (decreaseButton != null) decreaseButton.setOnClickListener(v -> updateQuantity(-1));
        if (increaseButton != null) increaseButton.setOnClickListener(v -> updateQuantity(1));
        if (addToCartButton != null) addToCartButton.setOnClickListener(v -> addToCart());
        if (addToFavoritesButton != null) addToFavoritesButton.setOnClickListener(v -> toggleFavoriteStatus());
        if (sendMessageButton != null) sendMessageButton.setOnClickListener(v -> openChat());
        arrowLeft.setOnClickListener(v -> imageSlider.setCurrentItem(imageSlider.getCurrentItem() - 1, true));
        arrowRight.setOnClickListener(v -> imageSlider.setCurrentItem(imageSlider.getCurrentItem() + 1, true));
    }

    private void loadProductDetails(int productId) {
        ApiClient.createTiendaApiService(this).getProductDetails(productId).enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(@NonNull Call<Producto> call, @NonNull Response<Producto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentProduct = response.body();
                    displayProductDetails();
                    updateUIVisibility();
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                    supportFinishAfterTransition();
                }
            }
            @Override
            public void onFailure(@NonNull Call<Producto> call, @NonNull Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                supportFinishAfterTransition();
            }
        });
    }

    private void loadReviews(int productId) {
        ApiClient.createTiendaApiService(this).getProductReviews(productId).enqueue(new Callback<List<Resena>>() {
            @Override
            public void onResponse(@NonNull Call<List<Resena>> call, @NonNull Response<List<Resena>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reviewList.clear();
                    reviewList.addAll(response.body());
                    reviewAdapter.notifyDataSetChanged();
                }
                updateReviewsVisibility();
            }
            @Override
            public void onFailure(@NonNull Call<List<Resena>> call, @NonNull Throwable t) {
                updateReviewsVisibility();
            }
        });
    }

    private void updateUIVisibility() {
        if (isReadOnly || currentProduct == null) {
            buyerView.setVisibility(View.GONE);
            modifyProductButton.setVisibility(View.GONE);
            addReviewButton.setVisibility(View.GONE);
            return;
        }
        boolean isOwner = sessionManager.isLoggedIn() && currentProduct.getUsuario() != null && (sessionManager.getUserId() == currentProduct.getUsuario().getIdUsuario());
        buyerView.setVisibility(isOwner ? View.GONE : View.VISIBLE);
        modifyProductButton.setVisibility(isOwner ? View.VISIBLE : View.GONE);
        addReviewButton.setVisibility(isOwner ? View.GONE : View.VISIBLE);
        if (!isOwner) {
            updateQuantity(0);
        }
    }

    private void showAddReviewDialog() {
        if (currentProduct == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_review, null);
        builder.setView(dialogView);
        final EditText commentInput = dialogView.findViewById(R.id.edit_comment);
        final RatingBar ratingBar = dialogView.findViewById(R.id.rating_bar);
        builder.setTitle("Escribir reseña")
                .setPositiveButton("Enviar", (dialog, id) -> {
                    int rating = (int) ratingBar.getRating();
                    String comment = commentInput.getText().toString().trim();
                    if (rating == 0 || comment.isEmpty()) {
                        Toast.makeText(this, "La puntuación y el comentario son obligatorios.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sendReview(rating, comment);
                })
                .setNegativeButton("Cancelar", null);
        builder.create().show();
    }

    private void sendReview(int rating, String comment) {
        ReviewRequest request = new ReviewRequest(currentProduct.getIdProducto(), rating, comment);
        ApiClient.createTiendaApiService(this).addReview(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProductDetailActivity.this, "Reseña enviada con éxito", Toast.LENGTH_SHORT).show();
                    loadReviews(currentProduct.getIdProducto());
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Error al enviar la reseña", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProductDetails() {
        if (currentProduct == null) return;
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(currentProduct.getTitulo());
        productNameView.setText(currentProduct.getTitulo());
        productPriceView.setText(String.format(Locale.GERMAN, "%.2f €", currentProduct.getPrecio()));
        productDescriptionView.setText(currentProduct.getDescripcion());
        
        if (currentProduct.getEstado() != null) {
            productStatusView.setText(currentProduct.getEstado());
            if ("Disponible".equalsIgnoreCase(currentProduct.getEstado())) {
                productStatusView.setTextColor(ContextCompat.getColor(this, R.color.status_available));
            } else {
                productStatusView.setTextColor(ContextCompat.getColor(this, R.color.status_unavailable));
            }
        }
        
        sellerNameView.setText(currentProduct.getUsuario() != null ? currentProduct.getUsuario().getNombre() : "Vendedor desconocido");

        List<String> images = currentProduct.getImagenes();
        if (images != null && !images.isEmpty()) {
            setupImageSlider(images);
        } else if (currentProduct.getImagenPrincipal() != null) {
            setupImageSlider(Collections.singletonList(currentProduct.getImagenPrincipal()));
        } else {
            imageSlider.setVisibility(View.GONE);
        }
        supportStartPostponedEnterTransition();
    }

    private void setupImageSlider(List<String> images) {
        ImageSliderAdapter sliderAdapter = new ImageSliderAdapter(this, images);
        imageSlider.setAdapter(sliderAdapter);
        boolean hasMultipleImages = images.size() > 1;
        arrowLeft.setVisibility(hasMultipleImages ? View.VISIBLE : View.GONE);
        arrowRight.setVisibility(hasMultipleImages ? View.VISIBLE : View.GONE);
        imageCounter.setVisibility(hasMultipleImages ? View.VISIBLE : View.GONE);
        if (hasMultipleImages) {
            imageSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    updateCarouselControls(position, images.size());
                }
            });
            updateCarouselControls(0, images.size());
        }
    }

    private void updateCarouselControls(int currentPosition, int totalImages) {
        imageCounter.setText(String.format(Locale.getDefault(), "%d / %d", currentPosition + 1, totalImages));
        arrowLeft.setEnabled(currentPosition > 0);
        arrowRight.setEnabled(currentPosition < totalImages - 1);
        arrowLeft.setAlpha(currentPosition > 0 ? 1.0f : 0.3f);
        arrowRight.setAlpha(currentPosition < totalImages - 1 ? 1.0f : 0.3f);
    }
    
    private void updateReviewsVisibility() {
        if(noReviewsText != null) noReviewsText.setVisibility(reviewList.isEmpty() ? View.VISIBLE : View.GONE);
        if(reviewsRecyclerView != null) reviewsRecyclerView.setVisibility(reviewList.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void updateQuantity(int change) {
        if (currentProduct == null) return;
        int newQuantity = currentQuantity + change;
        if (newQuantity < 0) newQuantity = 0;
        if (newQuantity > currentProduct.getCantidad()) {
            newQuantity = currentProduct.getCantidad();
            Toast.makeText(this, "Stock máximo alcanzado", Toast.LENGTH_SHORT).show();
        }
        currentQuantity = newQuantity;
        quantityTextView.setText(String.valueOf(currentQuantity));
        decreaseButton.setEnabled(currentQuantity > 0);
        increaseButton.setEnabled(currentQuantity < currentProduct.getCantidad());
        addToCartButton.setEnabled(currentQuantity > 0);
    }

    private void addToCart() {
        if (currentProduct != null && currentQuantity > 0) {
            CartManager.getInstance().addToCart(currentProduct, currentQuantity);
            Toast.makeText(this, currentQuantity + " x " + currentProduct.getTitulo() + " añadido al carrito", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleFavoriteStatus() {
        if (currentProduct == null) return;
        ApiClient.createTiendaApiService(this).toggleFavorite(new ToggleFavoriteRequest(currentProduct.getIdProducto())).enqueue(new Callback<Void>() {
            @Override public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    currentProduct.setFavorited(!currentProduct.isFavorited());
                    updateFavoriteButton();
                }
            }
            @Override public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {}
        });
    }

    private void updateFavoriteButton() {
        if (addToFavoritesButton != null) {
            addToFavoritesButton.setIconResource(currentProduct.isFavorited() ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);
        }
    }

    private void openChat() {
        if (currentProduct != null && currentProduct.getUsuario() != null) {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra(ChatActivity.EXTRA_OTHER_USER_ID, currentProduct.getUsuario().getIdUsuario());
            intent.putExtra(ChatActivity.EXTRA_PRODUCT_ID, currentProduct.getIdProducto());
            startActivity(intent);
        }
    }

    private void openEditProduct() {
        if (currentProduct != null) {
            Intent intent = new Intent(this, PublishProductActivity.class);
            intent.putExtra(PublishProductActivity.EXTRA_EDIT_PRODUCT_ID, currentProduct.getIdProducto());
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
