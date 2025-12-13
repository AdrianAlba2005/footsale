package com.example.footsale.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.auth0.android.jwt.JWT;

public class SessionManager {

    private static final String PREF_NAME = "FootSalePref";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_FCM_TOKEN = "fcm_token"; // Nuevo para Firebase

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveAuthToken(String token) {
        editor.putString(KEY_AUTH_TOKEN, token);
        
        try {
            JWT jwt = new JWT(token);
            Integer userId = jwt.getClaim("id_usuario").asInt();
            if (userId != null) {
                editor.putInt(KEY_USER_ID, userId);
            }
            
            String nombre = jwt.getClaim("nombre").asString();
            if (nombre != null) {
                editor.putString(KEY_USER_NAME, nombre);
            }
        } catch (Exception e) {
            Log.e("SessionManager", "Error decodificando token al guardar", e);
        }
        
        editor.apply();
    }

    public String getAuthToken() {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    public void saveUserId(int userId) {
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    public int getUserId() {
        int id = sharedPreferences.getInt(KEY_USER_ID, -1);
        
        if (id == -1) {
            String token = getAuthToken();
            if (token != null) {
                try {
                    JWT jwt = new JWT(token);
                    Integer userId = jwt.getClaim("id_usuario").asInt();
                    if (userId != null) {
                        id = userId;
                        saveUserId(id); 
                    }
                } catch (Exception e) {
                    Log.e("SessionManager", "Error recuperando ID del token", e);
                }
            }
        }
        return id;
    }
    
    public String getUserName() {
        String name = sharedPreferences.getString(KEY_USER_NAME, null);
        
        if (name == null) {
            String token = getAuthToken();
            if (token != null) {
                try {
                    JWT jwt = new JWT(token);
                    String nombre = jwt.getClaim("nombre").asString();
                    if (nombre != null) {
                        name = nombre;
                        editor.putString(KEY_USER_NAME, name);
                        editor.apply();
                    }
                } catch (Exception e) {
                    Log.e("SessionManager", "Error recuperando Nombre del token", e);
                }
            }
        }
        return name;
    }

    // Métodos para el token de Firebase
    public void saveFcmToken(String token) {
        editor.putString(KEY_FCM_TOKEN, token);
        editor.apply();
    }

    public String getFcmToken() {
        return sharedPreferences.getString(KEY_FCM_TOKEN, null);
    }

    public boolean isLoggedIn() {
        String authToken = getAuthToken();
        return authToken != null && !authToken.isEmpty();
    }

    public void clearSession() {
        String fcmToken = getFcmToken(); // Guardar el FCM token antes de borrar
        editor.clear();
        editor.putString(KEY_FCM_TOKEN, fcmToken); // Restaurarlo después de borrar
        editor.apply();
    }
}
