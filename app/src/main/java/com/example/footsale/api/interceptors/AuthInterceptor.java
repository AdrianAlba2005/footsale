package com.example.footsale.api.interceptors;

import android.content.Context;
import com.example.footsale.utils.SessionManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class  AuthInterceptor implements Interceptor {

    private SessionManager sessionManager;

    public AuthInterceptor(Context context) {
        sessionManager = new SessionManager(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();

        // Si tenemos un token, lo añadimos a la cabecera
        String token = sessionManager.getAuthToken();
        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer " + token);
        }

        return chain.proceed(requestBuilder.build());
    }
}