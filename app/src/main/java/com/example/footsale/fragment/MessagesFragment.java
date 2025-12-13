package com.example.footsale.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.auth0.android.jwt.JWT;
import com.example.footsale.R;
import com.example.footsale.adapter.ConversationAdapter;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.MessageApiService;
import com.example.footsale.entidades.Conversation; 
import com.example.footsale.utils.SessionManager;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ConversationAdapter adapter;
    private TextView emptyTextView;
    private int currentUserId = -1;
    private Handler handler = new Handler();
    private Runnable runnable;
    private boolean isFragmentActive = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        decodeCurrentUserId();
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupPeriodicRefresh();
    }

    private void decodeCurrentUserId() {
        if (getContext() == null) return;
        SessionManager sessionManager = new SessionManager(getContext());
        String token = sessionManager.getAuthToken();
        if (token != null && !token.isEmpty()) {
            try {
                JWT jwt = new JWT(token);
                Integer userId = jwt.getClaim("id_usuario").asInt();

                if (userId != null) {
                    this.currentUserId = userId;
                } else {
                    this.currentUserId = -1;
                }

            } catch (Exception e) {
                this.currentUserId = -1;
                Log.e("JWT_DECODE_ERROR", "Fallo al decodificar token en MessagesFragment", e);
            }
        }
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.conversationsRecyclerView);
        emptyTextView = view.findViewById(R.id.emptyMessagesText);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void loadConversations() {
        if (getContext() == null || !isFragmentActive) return;

        MessageApiService apiService = ApiClient.createMensajeApiService(getContext());

        apiService.getConversations().enqueue(new Callback<List<Conversation>>() {
            @Override
            public void onResponse(@NonNull Call<List<Conversation>> call, @NonNull Response<List<Conversation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Conversation> conversations = response.body();
                    if (!conversations.isEmpty()) {
                        showConversations(conversations);
                    } else {
                        showEmptyView();
                    }
                } else {
                    showEmptyView();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Conversation>> call, @NonNull Throwable t) {
                // Si la red falla y no hay datos previos, mostrar vacío, pero evitar borrar la lista si ya existe
                if (adapter == null || adapter.getItemCount() == 0) {
                     showEmptyView();
                }
                Log.e("API_ERROR", "Error detallado: " + t.getMessage());
            }
        });
    }

    private void showConversations(List<Conversation> conversations) {
        if (!isFragmentActive) return;
        
        recyclerView.setVisibility(View.VISIBLE);
        emptyTextView.setVisibility(View.GONE);
        
        // Optimización: Si el adaptador ya existe, actualizar los datos en lugar de recrearlo
        if (adapter == null) {
            adapter = new ConversationAdapter(getContext(), conversations, currentUserId);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(conversations);
        }
    }

    private void showEmptyView() {
        if (!isFragmentActive) return;
        recyclerView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.VISIBLE);
    }
    
    private void setupPeriodicRefresh() {
        runnable = new Runnable() {
            @Override
            public void run() {
                loadConversations();
                if (isFragmentActive) {
                    handler.postDelayed(this, 5000); // Actualizar cada 5 segundos
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        isFragmentActive = true;
        decodeCurrentUserId(); // Asegurar ID actualizado
        loadConversations(); // Cargar inmediatamente al volver
        handler.post(runnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        isFragmentActive = false;
        handler.removeCallbacks(runnable);
    }
}
