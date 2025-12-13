package com.example.footsale;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.UsuarioApiService;
import com.example.footsale.api.models.UpdateProfileRequest;
import com.example.footsale.entidades.Usuario;
import com.hbb20.CountryCodePicker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    // CORREGIDO: Nombres de variables para que coincidan con la intención
    private EditText nameEditText, phoneEditText, addressEditText, cityEditText;
    private ImageView profileImageView;
    private TextView changePhotoText; // Para el texto "Cambiar foto"
    private CountryCodePicker nationalityPicker, phoneCodePicker;
    private Button saveButton;
    private Uri imageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initToolbar();
        initViews();

        profileImageView.setOnClickListener(v -> openImageChooser());
        changePhotoText.setOnClickListener(v -> openImageChooser());
        saveButton.setOnClickListener(v -> updateProfile());

        loadUserProfile();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Editar Perfil");
        }
    }

    private void initViews() {
        // CORREGIDO: Usar los IDs correctos del layout XML
        nameEditText = findViewById(R.id.editTextName);
        phoneEditText = findViewById(R.id.editTextPhone);
        addressEditText = findViewById(R.id.editTextAddress);
        cityEditText = findViewById(R.id.editTextCity);
        profileImageView = findViewById(R.id.profileAvatar);
        changePhotoText = findViewById(R.id.changePhotoText);
        nationalityPicker = findViewById(R.id.nationalityPicker);
        phoneCodePicker = findViewById(R.id.phoneCodePicker);
        saveButton = findViewById(R.id.btnSaveChanges);
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(profileImageView);
        }
    }

    private void loadUserProfile() {
        ApiClient.createUsuarioApiService(this).getProfileInfo().enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario user = response.body();
                    nameEditText.setText(user.getNombre());
                    // Aquí iría la lógica para rellenar los otros campos
                    if (user.getFotoPerfil() != null && !user.getFotoPerfil().isEmpty()) {
                        Glide.with(EditProfileActivity.this).load(user.getFotoPerfil()).into(profileImageView);
                    }
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {}
        });
    }

    private void updateProfile() {
        String name = nameEditText.getText().toString();
        // El email no se puede editar, así que se omite del envío

        UsuarioApiService apiService = ApiClient.createUsuarioApiService(this);
        Call<Void> call;

        if (imageUri != null) {
            // Lógica para subir imagen con otros campos (requiere backend multipart)
            Toast.makeText(this, "La subida de imagen aún no está implementada en el servidor.", Toast.LENGTH_SHORT).show();
        } else {
            // Lógica para actualizar solo texto
            Map<String, RequestBody> fields = new HashMap<>();
            fields.put("nombre", RequestBody.create(MediaType.parse("text/plain"), name));
            // Aquí se añadirían los otros campos de texto a actualizar

            // Esto debería llamar a un endpoint que no requiera imagen
            // De momento lo dejamos apuntando a updateProfile, pero el backend debería manejarlo
            call = apiService.updateProfile(fields, null);
            call.enqueue(new Callback<Void>() {
                 @Override
                 public void onResponse(Call<Void> call, Response<Void> response) {
                     if (response.isSuccessful()) {
                         Toast.makeText(EditProfileActivity.this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
                         finish();
                     } else {
                         Toast.makeText(EditProfileActivity.this, "Error al actualizar el perfil", Toast.LENGTH_SHORT).show();
                     }
                 }

                 @Override
                 public void onFailure(Call<Void> call, Throwable t) {
                     Toast.makeText(EditProfileActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                 }
            });
        }
    }
}
