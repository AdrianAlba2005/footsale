package com.example.footsale.api;

import android.content.Context;
import com.example.footsale.utils.SessionManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "http://10.0.2.2/footsale/";
    private static Retrofit retrofit = null;

    private static Retrofit getClient(Context context) {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);

            // --- CORRECCIÓN: Interceptor para limpiar respuestas malformadas de PHP (?>) ---
            httpClient.addInterceptor(chain -> {
                okhttp3.Response response = chain.proceed(chain.request());
                if (response.body() != null) {
                    okhttp3.MediaType contentType = response.body().contentType();
                    // Solo intentamos limpiar si es una respuesta JSON
                    if (contentType != null && contentType.toString().contains("json")) {
                        String content = response.body().string();
                        // Si empieza con ?>, lo quitamos
                        if (content.trim().startsWith("?>")) {
                            content = content.trim().substring(2).trim();
                        }
                        // Reconstruimos la respuesta limpia
                        okhttp3.ResponseBody body = okhttp3.ResponseBody.create(contentType, content);
                        return response.newBuilder().body(body).build();
                    }
                }
                return response;
            });
            // -------------------------------------------------------------------------------

            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();
                String token = new SessionManager(context).getAuthToken();
                if (token != null) {
                    requestBuilder.header("Authorization", "Bearer " + token);
                }
                return chain.proceed(requestBuilder.build());
            });

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }

    public static UsuarioApiService createUsuarioApiService(Context context) {
        return getClient(context).create(UsuarioApiService.class);
    }

    public static TiendaApiService createTiendaApiService(Context context) {
        return getClient(context).create(TiendaApiService.class);
    }

    public static ChatApiService createChatApiService(Context context) {
        return getClient(context).create(ChatApiService.class);
    }

    public static MessageApiService createMensajeApiService(Context context) {
        return getClient(context).create(MessageApiService.class);
    }

    public static FirebaseApiService createFirebaseApiService(Context context) {
        return getClient(context).create(FirebaseApiService.class);
    }

    public static void resetClient() {
        retrofit = null;
    }

    public static String getFullImageUrl(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) return null;

        // Si la URL viene con localhost (común en entornos de desarrollo), cambiar a 10.0.2.2 para el emulador
        if (imagePath.contains("localhost")) {
            imagePath = imagePath.replace("localhost", "10.0.2.2");
        }

        if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
            return imagePath;
        }
        
        // Evitar doble barra si la ruta ya empieza por /
        if (imagePath.startsWith("/")) {
            return BASE_URL.substring(0, BASE_URL.length() - 1) + imagePath;
        }
        return BASE_URL + imagePath;
    }
}
