package com.example.footsale.api;

import com.example.footsale.api.models.AddCardRequest;
import com.example.footsale.api.models.BanUserRequest;
import com.example.footsale.api.models.DashboardStats;
import com.example.footsale.api.models.DeleteCardRequest;
import com.example.footsale.api.models.EmailRequest;
import com.example.footsale.api.models.LoginRequest;
import com.example.footsale.api.models.LoginResponse;
import com.example.footsale.api.models.RegisterRequest;
import com.example.footsale.api.models.ResetPasswordRequest;
import com.example.footsale.api.models.UpdateProfileRequest;
import com.example.footsale.api.models.VerifyCodeRequest;
import com.example.footsale.api.models.WithdrawRequest;
import com.example.footsale.entidades.Card;
import com.example.footsale.entidades.Pedido;
import com.example.footsale.entidades.Usuario;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface UsuarioApiService {

    @POST("usuario.php?action=register")
    Call<Void> register(@Body RegisterRequest request);

    @POST("usuario.php?action=login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("usuario.php?action=get_profile")
    Call<Usuario> getProfileInfo();

    @GET("usuario.php?action=get_public_profile")
    Call<Usuario> getUserProfile(@Query("user_id") int userId);

    @Multipart
    @POST("usuario.php?action=update_profile")
    Call<Void> updateProfile(@PartMap Map<String, RequestBody> fields, @Part MultipartBody.Part image);

    // Cambiado a @FormUrlEncoded para que PHP reciba los datos en $_POST
    @FormUrlEncoded
    @POST("usuario.php?action=update_profile")
    Call<Void> updateProfileTextOnly(
            @Field("nombre") String nombre,
            @Field("nacionalidad") String nacionalidad,
            @Field("telefono") String telefono,
            @Field("calle") String calle,
            @Field("ciudad") String ciudad
    );

    @POST("usuario.php?action=request_password_reset")
    Call<Void> requestPasswordReset(@Body EmailRequest request);

    @POST("usuario.php?action=verify_email")
    Call<Void> verifyCode(@Body VerifyCodeRequest request);

    @POST("usuario.php?action=reset_password")
    Call<Void> resetPassword(@Body ResetPasswordRequest request);

    @POST("usuario.php?action=add_card")
    Call<Void> addCard(@Body AddCardRequest request);

    @GET("usuario.php?action=get_my_cards")
    Call<List<Card>> getMyCards();

    @POST("usuario.php?action=delete_card")
    Call<Void> deleteCard(@Body DeleteCardRequest request);

    @POST("usuario.php?action=withdraw_balance")
    Call<Void> withdrawBalance(@Body WithdrawRequest request);

    @GET("usuario.php?action=get_statistics")
    Call<DashboardStats> getStatistics();

    @GET("usuario.php?action=get_all_users")
    Call<List<Usuario>> getAllUsers();

    @POST("usuario.php?action=ban_user")
    Call<Void> banUser(@Body BanUserRequest request);

    @GET("usuario.php?action=get_all_orders")
    Call<List<Pedido>> getAllOrders();
}
