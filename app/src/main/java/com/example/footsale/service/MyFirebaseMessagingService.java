package com.example.footsale.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.footsale.ChatActivity;
import com.example.footsale.MainActivity;
import com.example.footsale.R;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.FirebaseApiService;
import com.example.footsale.utils.SessionManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            Log.d(TAG, "Message data payload: " + data);

            String type = data.get("type");
            if ("chat_message".equals(type)) {
                // Intentar notificar a la actividad de chat si está abierta
                boolean handled = notifyActivity(data);
                // Si no fue manejado por la actividad, mostrar notificación de sistema
                if (!handled) {
                    showChatNotification(remoteMessage.getNotification(), data);
                }
            } else {
                showGenericNotification(remoteMessage.getNotification());
            }
        } else if (remoteMessage.getNotification() != null) {
            showGenericNotification(remoteMessage.getNotification());
        }
    }

    private boolean notifyActivity(Map<String, String> data) {
        Intent intent = new Intent("new_chat_message");
        String senderId = data.get("sender_id");
        if (senderId != null) {
            intent.putExtra("sender_id", Integer.parseInt(senderId));
        }
        // Usar LocalBroadcastManager es más seguro y eficiente
        return LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);
        new SessionManager(getApplicationContext()).saveFcmToken(token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        if (!new SessionManager(this).isLoggedIn()) return;

        FirebaseApiService.UpdateTokenRequest request = new FirebaseApiService.UpdateTokenRequest(token);
        ApiClient.createFirebaseApiService(this).updateFcmToken(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "FCM token updated on server.");
                } else {
                    Log.e(TAG, "Failed to update FCM token on server: " + response.code());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e(TAG, "Network error updating FCM token: " + t.getMessage());
            }
        });
    }

    private void showChatNotification(RemoteMessage.Notification notification, Map<String, String> data) {
        if (notification == null) return;
        int senderId = Integer.parseInt(data.get("sender_id"));

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_OTHER_USER_ID, senderId);
        intent.putExtra(ChatActivity.EXTRA_OTHER_USER_NAME, data.get("sender_name"));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, senderId, intent, 
            PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        showNotification(notification.getTitle(), notification.getBody(), pendingIntent);
    }

    private void showGenericNotification(RemoteMessage.Notification notification) {
        if (notification == null) return;
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 
            PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        
        showNotification(notification.getTitle(), notification.getBody(), pendingIntent);
    }

    private void showNotification(String title, String body, PendingIntent pendingIntent) {
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_logo)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Mensajes y Alertas", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
    }
}
