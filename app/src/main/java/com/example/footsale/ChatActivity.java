package com.example.footsale;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.footsale.adapter.MessageAdapter;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.ChatApiService;
import com.example.footsale.entidades.Message;
import com.example.footsale.entidades.Usuario;
import com.example.footsale.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    public static final String EXTRA_OTHER_USER_ID = "other_user_id";
    public static final String EXTRA_OTHER_USER_NAME = "other_user_name";
    public static final String EXTRA_OTHER_USER_IMAGE = "other_user_image";
    public static final String EXTRA_PRODUCT_ID = "product_id";

    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<Message> messageList = new ArrayList<>();
    private EditText messageEditText;
    private ImageButton sendButton;
    private int otherUserId;
    private int productId;
    private Handler handler = new Handler();
    private Runnable runnable;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sessionManager = new SessionManager(this);
        otherUserId = getIntent().getIntExtra(EXTRA_OTHER_USER_ID, -1);
        productId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, -1);

        if (otherUserId == -1 || otherUserId == sessionManager.getUserId()) {
            Toast.makeText(this, "Error: Usuario de chat no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initToolbar();
        initViews();
        setupRecyclerView();

        // Configurar info inicial desde intent si está disponible, pero siempre priorizar la carga de red
        // para asegurar que los datos sean correctos y del OTRO usuario.
        loadOtherUserInfo(); 

        setupPeriodicRefresh();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.chat_toolbar);
        if (toolbar == null) {
            View includeLayout = findViewById(R.id.chat_toolbar_layout);
            if (includeLayout instanceof Toolbar) {
                toolbar = (Toolbar) includeLayout;
            }
        }
        
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        View toolbarContent = findViewById(R.id.toolbar_content_layout);
        if(toolbarContent != null) {
            toolbarContent.setOnClickListener(v -> {
                Log.d("ChatActivity", "Opening profile for user ID: " + otherUserId);
                Intent intent = new Intent(ChatActivity.this, UserProfileActivity.class);
                intent.putExtra(UserProfileActivity.EXTRA_USER_ID, otherUserId);
                startActivity(intent);
            });
        }
        
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.chatRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void setupRecyclerView() {
        int currentUserId = sessionManager.getUserId();
        adapter = new MessageAdapter(this, messageList, currentUserId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void loadOtherUserInfo() {
        // Cargar datos iniciales desde el Intent mientras se obtienen de la red
        String intentName = getIntent().getStringExtra(EXTRA_OTHER_USER_NAME);
        String intentImage = getIntent().getStringExtra(EXTRA_OTHER_USER_IMAGE);
        if (intentName != null) {
            updateToolbar(intentName, intentImage);
        }

        // Esta es la parte crítica: Solicitar al servidor los datos del 'otherUserId'
        // explícitamente para llenar la barra superior.
        ApiClient.createUsuarioApiService(this).getUserProfile(otherUserId).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario user = response.body();
                    
                    // VALIDACIÓN IMPORTANTE: 
                    // Verificar que el usuario recibido NO sea el usuario actual.
                    // Si el servidor ignora el parámetro id_usuario y devuelve el perfil del token (usuario actual),
                    // no debemos sobrescribir la barra con nuestros propios datos.
                    if (user.getIdUsuario() != sessionManager.getUserId()) {
                        updateToolbar(user.getNombre(), user.getFotoPerfil());
                    } else {
                        Log.w("ChatActivity", "Server returned current user profile instead of target user. Keeping intent data.");
                    }
                } else {
                    Log.e("ChatActivity", "Error loading user info: " + response.code());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                Log.e("ChatActivity", "Network error loading user info", t);
            }
        });
    }

    private void updateToolbar(String name, String imageUrl) {
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        CircleImageView toolbarImage = findViewById(R.id.toolbar_image);

        if (toolbarTitle != null) {
            toolbarTitle.setText(name);
        }
        
        if (toolbarImage != null) {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                // Asegurarse de que la URL sea absoluta si es relativa
                if (!imageUrl.startsWith("http")) {
                    imageUrl = "http://10.0.2.2/footsale/" + imageUrl;
                }
                
                Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(toolbarImage);
            } else {
                toolbarImage.setImageResource(R.drawable.ic_person);
            }
        }
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (messageText.isEmpty()) return;

        ChatApiService.SendMessageRequest request = new ChatApiService.SendMessageRequest(otherUserId, messageText, productId);
        ApiClient.createChatApiService(this).sendMessage(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    messageEditText.setText("");
                    loadMessages();
                } else {
                    Toast.makeText(ChatActivity.this, "Error enviando mensaje", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMessages() {
        ApiClient.createChatApiService(this).getMessages(otherUserId).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    messageList.clear();
                    messageList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    if (!messageList.isEmpty()) {
                        recyclerView.scrollToPosition(messageList.size() - 1);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.e("ChatActivity", "Error loading messages", t);
            }
        });
    }
    
    private void setupPeriodicRefresh() {
        runnable = new Runnable() {
            @Override
            public void run() {
                loadMessages();
                handler.postDelayed(this, 5000);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMessages();
        handler.post(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}
