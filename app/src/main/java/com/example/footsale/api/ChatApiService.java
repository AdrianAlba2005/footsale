package com.example.footsale.api;

import com.example.footsale.entidades.Message;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ChatApiService {

    // CORREGIDO: Apuntar a mensaje.php
    @GET("mensaje.php?action=get_messages")
    Call<List<Message>> getMessages(@Query("other_user_id") int otherUserId);

    // CORREGIDO: Apuntar a mensaje.php
    @POST("mensaje.php?action=send_message")
    Call<Void> sendMessage(@Body SendMessageRequest request);

    class SendMessageRequest {
        @SerializedName("id_destinatario")
        private final int receiverId;

        @SerializedName("mensaje")
        private final String message;

        @SerializedName("id_producto")
        private final int productId;

        public SendMessageRequest(int receiverId, String message, int productId) {
            this.receiverId = receiverId;
            this.message = message;
            this.productId = productId;
        }
    }
}
