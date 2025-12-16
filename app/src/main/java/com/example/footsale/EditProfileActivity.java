package com.example.footsale;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.example.footsale.entidades.Usuario;
import com.hbb20.CountryCodePicker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "EditProfileActivity";

    private EditText nameEditText, phoneEditText, addressEditText;
    private AutoCompleteTextView cityEditText;
    private ImageView profileImageView;
    private TextView changePhotoText;
    private CountryCodePicker nationalityPicker, phoneCodePicker;
    private Button saveButton;
    private Uri imageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initToolbar();
        initViews();
        setupCityDropdown();

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
        nameEditText = findViewById(R.id.editTextName);
        phoneEditText = findViewById(R.id.editTextPhone);
        addressEditText = findViewById(R.id.editTextAddress);
        cityEditText = findViewById(R.id.editTextCity);
        profileImageView = findViewById(R.id.profileAvatar);
        changePhotoText = findViewById(R.id.changePhotoText);
        nationalityPicker = findViewById(R.id.nationalityPicker);
        phoneCodePicker = findViewById(R.id.phoneCodePicker);
        saveButton = findViewById(R.id.btnSaveChanges);

        phoneCodePicker.registerCarrierNumberEditText(phoneEditText);
    }

    private void setupCityDropdown() {
        String[] cityTypes = new String[]{"PUEBLO", "CAPITAL", "PEDANIA"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, cityTypes);
        cityEditText.setAdapter(adapter);
        cityEditText.setEnabled(true);

        cityEditText.setOnClickListener(v -> cityEditText.showDropDown());
        cityEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) cityEditText.showDropDown();
        });
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

                    Log.d(TAG, "Datos recibidos -> Nombre: " + user.getNombre() + ", Tel: " + user.getTelefono() + ", Calle: " + user.getCalle() + ", Ciudad: " + user.getCiudad() + ", Nac: " + user.getNacionalidad());
                    
                    if (user.getNombre() != null) nameEditText.setText(user.getNombre());
                    if (user.getTelefono() != null) {
                        phoneCodePicker.setFullNumber(user.getTelefono()); 
                    }
                    if (user.getCalle() != null) addressEditText.setText(user.getCalle());
                    if (user.getCiudad() != null) cityEditText.setText(user.getCiudad(), false);

                    if (user.getNacionalidad() != null && !user.getNacionalidad().isEmpty()) {
                        String storedCountry = user.getNacionalidad();
                        String isoCodeFound = null;
                        for (String iso : Locale.getISOCountries()) {
                            Locale l = new Locale("", iso);
                            if (l.getDisplayCountry().equalsIgnoreCase(storedCountry) || 
                                l.getDisplayCountry(Locale.ENGLISH).equalsIgnoreCase(storedCountry) ||
                                l.getDisplayCountry(new Locale("es")).equalsIgnoreCase(storedCountry)) {
                                isoCodeFound = iso;
                                break;
                            }
                        }

                        if (isoCodeFound != null) {
                            nationalityPicker.setCountryForNameCode(isoCodeFound);
                        }
                    }

                    if (user.getFotoPerfil() != null && !user.getFotoPerfil().isEmpty()) {
                        Glide.with(EditProfileActivity.this)
                            .load(ApiClient.getFullImageUrl(user.getFotoPerfil()))
                            .into(profileImageView);
                    }
                } else {
                    Log.e(TAG, "Error al cargar perfil: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e(TAG, "Fallo de red al cargar perfil", t);
            }
        });
    }

    private void updateProfile() {
        String name = nameEditText.getText().toString();
        String phone = phoneCodePicker.getFullNumberWithPlus(); 
        String address = addressEditText.getText().toString();
        String city = cityEditText.getText().toString();
        String nationality = nationalityPicker.getSelectedCountryName(); 

        UsuarioApiService apiService = ApiClient.createUsuarioApiService(this);
        Call<Void> call;

        if (imageUri != null) {
            try {
                File file = new File(getCacheDir(), "temp_image");
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                OutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.close();
                inputStream.close();

                RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("profile_photo", file.getName(), requestFile);

                Map<String, RequestBody> fields = new HashMap<>();
                fields.put("nombre", RequestBody.create(MediaType.parse("text/plain"), name));
                fields.put("telefono", RequestBody.create(MediaType.parse("text/plain"), phone));
                fields.put("calle", RequestBody.create(MediaType.parse("text/plain"), address));
                fields.put("ciudad", RequestBody.create(MediaType.parse("text/plain"), city));
                fields.put("nacionalidad", RequestBody.create(MediaType.parse("text/plain"), nationality));

                call = apiService.updateProfile(fields, body);

            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        } else {
            call = apiService.updateProfileTextOnly(name, nationality, phone, address, city);
        }

        call.enqueue(new Callback<Void>() {
             @Override
             public void onResponse(Call<Void> call, Response<Void> response) {
                 if (response.isSuccessful()) {
                     Toast.makeText(EditProfileActivity.this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
                     finish();
                 } else {
                     Toast.makeText(EditProfileActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                 }
             }

             @Override
             public void onFailure(Call<Void> call, Throwable t) {
                 Toast.makeText(EditProfileActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
             }
        });
    }
}
