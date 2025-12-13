package com.example.footsale.api;

import com.example.footsale.api.models.AddCardRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PaymentApiService {

    @POST("your/endpoint/for/adding/cards") // <-- IMPORTANT: Replace with your actual API endpoint path
    Call<Void> addCard(@Body AddCardRequest addCardRequest);

}
