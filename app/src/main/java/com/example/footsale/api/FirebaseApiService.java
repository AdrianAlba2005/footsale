package com.example.footsale.api;

import com.google.gson.annotations.SerializedName;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FirebaseApiService {

    @POST("firebase.php?action=update_fcm_token")
    Call<Void> updateFcmToken(@Body UpdateTokenRequest request);

    class UpdateTokenRequest {
        @SerializedName("fcm_token")
        private String fcmToken;

        public UpdateTokenRequest(String fcmToken) {
            this.fcmToken = fcmToken;
        }
    }
}
