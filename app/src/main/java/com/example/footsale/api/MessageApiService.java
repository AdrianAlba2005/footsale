package com.example.footsale.api;

import com.example.footsale.entidades.Conversation;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MessageApiService {

    // CORREGIDO: Apuntar a mensaje.php
    @GET("mensaje.php?action=get_conversations")
    Call<List<Conversation>> getConversations();
}
