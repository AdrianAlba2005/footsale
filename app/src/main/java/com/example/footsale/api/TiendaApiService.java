package com.example.footsale.api;

import com.example.footsale.api.models.CheckoutSummary;
import com.example.footsale.api.models.CheckoutSummaryRequest;
import com.example.footsale.api.models.DeleteProductRequest;
import com.example.footsale.api.models.DeleteReviewRequest;
import com.example.footsale.api.models.ProcessCardOrderRequest;
import com.example.footsale.api.models.ReviewRequest;
import com.example.footsale.api.models.ToggleFavoriteRequest;
import com.example.footsale.entidades.Categoria;
import com.example.footsale.entidades.Pedido;
import com.example.footsale.entidades.Producto;
import com.example.footsale.entidades.Resena;
import com.example.footsale.entidades.Venta;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface TiendaApiService {

    @GET("tienda.php?action=get_all_products")
    Call<List<Producto>> getAllProducts(@Query("exclude_user_id") int userId, @Query("page") int page);

    @GET("tienda.php?action=get_my_products")
    Call<List<Producto>> getMyProducts();

    @GET("tienda.php?action=get_product_details")
    Call<Producto> getProductDetails(@Query("id_producto") int productId);

    @GET("tienda.php?action=search_products")
    Call<List<Producto>> searchProducts(@Query("query") String searchQuery, @Query("exclude_user_id") int userId, @Query("page") int page);

    @GET("tienda.php?action=get_products_by_category")
    Call<List<Producto>> getProductsByCategory(@Query("category_id") int categoryId, @Query("exclude_user_id") int userId, @Query("page") int page);

    @Multipart
    @POST("tienda.php?action=create_product")
    Call<Void> createProduct(
            @Part("titulo") RequestBody titulo,
            @Part("descripcion") RequestBody descripcion,
            @Part("precio") RequestBody precio,
            @Part("cantidad") RequestBody cantidad,
            @Part("id_categoria") RequestBody idCategoria,
            @Part List<MultipartBody.Part> imagenes
    );

    @Multipart
    @POST("tienda.php?action=update_product")
    Call<Void> updateProduct(
            @Part("id_producto") RequestBody idProducto,
            @Part("titulo") RequestBody titulo,
            @Part("descripcion") RequestBody descripcion,
            @Part("precio") RequestBody precio,
            @Part("cantidad") RequestBody cantidad,
            @Part("id_categoria") RequestBody idCategoria,
            @Part List<MultipartBody.Part> imagenes
    );

    @POST("tienda.php?action=delete_product")
    Call<Void> deleteProduct(@Body DeleteProductRequest request);

    @GET("tienda.php?action=get_categories")
    Call<List<Categoria>> getCategories();

    @GET("tienda.php?action=get_my_favorites")
    Call<List<Producto>> getMyFavorites();

    @POST("tienda.php?action=toggle_favorite")
    Call<Void> toggleFavorite(@Body ToggleFavoriteRequest request);

    @POST("tienda.php?action=get_checkout_summary")
    Call<CheckoutSummary> getCheckoutSummary(@Body CheckoutSummaryRequest request);

    @POST("tienda.php?action=process_order")
    Call<Void> processCardOrder(@Body ProcessCardOrderRequest request);

    @GET("tienda.php?action=get_purchase_history")
    Call<List<Pedido>> getPurchaseHistory();

    @GET("tienda.php?action=get_sales_history")
    Call<List<Venta>> getSalesHistory();

    @POST("tienda.php?action=add_review")
    Call<Void> addReview(@Body ReviewRequest request);

    @POST("tienda.php?action=delete_review")
    Call<Void> deleteReview(@Body DeleteReviewRequest request);

    @GET("tienda.php?action=get_user_reviews")
    Call<List<Resena>> getUserReviews(@Query("id_usuario") int userId);
    
    @GET("tienda.php?action=get_user_reviews")
    Call<List<Resena>> getProductReviews(@Query("id_producto") int productId);
}
