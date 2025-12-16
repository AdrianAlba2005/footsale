package com.example.footsale;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footsale.adapter.ImagePreviewAdapter;
import com.example.footsale.api.ApiClient;
import com.example.footsale.entidades.Categoria;
import com.example.footsale.entidades.Producto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublishProductActivity extends AppCompatActivity implements ImagePreviewAdapter.OnImageRemoveListener {

    public static final String EXTRA_EDIT_PRODUCT_ID = "edit_product_id";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "PublishProductActivity";

    private EditText titleEditText, descriptionEditText, priceEditText, quantityEditText;
    private Spinner categorySpinner;
    private Button selectImagesButton, publishButton;
    private RecyclerView imagesRecyclerView;

    private List<Categoria> categoriaList = new ArrayList<>();
    private List<Uri> imageUris = new ArrayList<>();
    private ImagePreviewAdapter imagePreviewAdapter;
    private int productIdToEdit = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_product);

        initViews();
        setupAdapters();
        loadCategories();

        if (getIntent().hasExtra(EXTRA_EDIT_PRODUCT_ID)) {
            productIdToEdit = getIntent().getIntExtra(EXTRA_EDIT_PRODUCT_ID, -1);
            if (productIdToEdit != -1) {
                loadProductForEdit(productIdToEdit);
                publishButton.setText("Actualizar Producto");
            }
        }

        selectImagesButton.setOnClickListener(v -> openImageChooser());
        publishButton.setOnClickListener(v -> publishProduct());
    }

    private void initViews() {
        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        priceEditText = findViewById(R.id.priceEditText);
        quantityEditText = findViewById(R.id.quantityEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        selectImagesButton = findViewById(R.id.selectImagesButton);
        publishButton = findViewById(R.id.publishButton);
        imagesRecyclerView = findViewById(R.id.imagesRecyclerView);
    }

    private void setupAdapters() {
        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imagePreviewAdapter = new ImagePreviewAdapter(this, imageUris, this);
        imagesRecyclerView.setAdapter(imagePreviewAdapter);
    }

    @Override
    public void onImageRemoved(int position) {
        if (position >= 0 && position < imageUris.size()) {
            imageUris.remove(position);
            imagePreviewAdapter.notifyItemRemoved(position);
            imagePreviewAdapter.notifyItemRangeChanged(position, imageUris.size());
        }
    }

    private void loadCategories() {
        ApiClient.createTiendaApiService(this).getCategories().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoriaList = response.body();
                    List<String> categoryNames = new ArrayList<>();
                    for (Categoria categoria : categoriaList) {
                        categoryNames.add(categoria.getNombre());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(PublishProductActivity.this, android.R.layout.simple_spinner_item, categoryNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {}
        });
    }

    private void loadProductForEdit(int productId) {
        ApiClient.createTiendaApiService(this).getProductDetails(productId).enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Producto producto = response.body();
                    titleEditText.setText(producto.getTitulo());
                    descriptionEditText.setText(producto.getDescripcion());
                    priceEditText.setText(String.valueOf(producto.getPrecio()));
                    quantityEditText.setText(String.valueOf(producto.getCantidad()));

                    if (producto.getCategoria() != null) {
                        for (int i = 0; i < categoriaList.size(); i++) {
                            if (categoriaList.get(i).getId_categoria() == producto.getCategoria().getId_categoria()) {
                                categorySpinner.setSelection(i);
                                break;
                            }
                        }
                    }

                    if (producto.getImagenes() != null && !producto.getImagenes().isEmpty()) {
                        imageUris.clear();
                        for (String imageUrl : producto.getImagenes()) {
                            imageUris.add(Uri.parse(ApiClient.getFullImageUrl(imageUrl)));
                        }
                        imagePreviewAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onFailure(Call<Producto> call, Throwable t) {}
        });
    }
    
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar Imágenes"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    if (imageUris.size() < 9) imageUris.add(clipData.getItemAt(i).getUri());
                }
            } else if (data.getData() != null) {
                if (imageUris.size() < 9) imageUris.add(data.getData());
            }
            imagePreviewAdapter.notifyDataSetChanged();
        }
    }

    private void publishProduct() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();
        String quantity = quantityEditText.getText().toString().trim();
        
        if (categoriaList.isEmpty()) return;
        Categoria selectedCategory = categoriaList.get(categorySpinner.getSelectedItemPosition());
        int catId = selectedCategory.getId_categoria();

        // Mostrar indicador de carga y deshabilitar botón para evitar dobles clics
        publishButton.setEnabled(false);
        Toast.makeText(this, "Procesando imágenes, por favor espere...", Toast.LENGTH_SHORT).show();

        // Ejecutar la preparación de imágenes (descarga y copiado) en segundo plano
        new Thread(() -> {
            List<MultipartBody.Part> imageParts = prepareImageParts();
            
            // Volver al hilo principal para realizar la petición de red con Retrofit
            runOnUiThread(() -> {
                publishButton.setEnabled(true);
                if (productIdToEdit != -1) {
                    updateProductMultipart(productIdToEdit, title, description, price, quantity, catId, imageParts);
                } else {
                    createProductMultipart(title, description, price, quantity, catId, imageParts);
                }
            });
        }).start();
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = cR.getType(uri);
        return mime.getExtensionFromMimeType(type);
    }

    private File downloadFileFromUrl(String urlString) {
        try {
            java.net.URL url = new java.net.URL(urlString);
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            File file = new File(getCacheDir(), "reupload_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream output = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int len;
            while ((len = input.read(buffer)) > 0) {
                output.write(buffer, 0, len);
            }
            output.close();
            input.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<MultipartBody.Part> prepareImageParts() {
        List<MultipartBody.Part> imageParts = new ArrayList<>();
        for (Uri uri : imageUris) {
            File file = null;
            try {
                if (uri.toString().startsWith("http")) {
                    // Si es una URL remota, la descargamos para re-subirla
                    file = downloadFileFromUrl(uri.toString());
                } else {
                    // Si es local, la copiamos al cache
                    String extension = getFileExtension(uri);
                    if (extension == null) extension = "jpg";

                    file = new File(getCacheDir(), "temp_image_" + System.currentTimeMillis() + "." + extension);
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    FileOutputStream outputStream = new FileOutputStream(file);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buf)) > 0) outputStream.write(buf, 0, len);
                    outputStream.close();
                    inputStream.close();
                }

                if (file != null && file.exists()) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
                    // IMPORTANTE: El nombre del archivo debe tener extensión
                    imageParts.add(MultipartBody.Part.createFormData("imagenes[]", file.getName(), requestFile));
                    Log.d(TAG, "Imagen preparada: " + file.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return imageParts;
    }

    private void createProductMultipart(String title, String desc, String price, String quant, int catId, List<MultipartBody.Part> imageParts) {
        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);
        RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), price);
        RequestBody quantBody = RequestBody.create(MediaType.parse("text/plain"), quant);
        RequestBody catIdBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(catId));

        ApiClient.createTiendaApiService(this).createProduct(titleBody, descBody, priceBody, quantBody, catIdBody, imageParts).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PublishProductActivity.this, "Producto publicado con éxito", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PublishProductActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(PublishProductActivity.this, "Error al publicar el producto", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PublishProductActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProductMultipart(int productId, String title, String desc, String price, String quant, int catId, List<MultipartBody.Part> imageParts) {
        RequestBody idBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(productId));
        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);
        RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), price);
        RequestBody quantBody = RequestBody.create(MediaType.parse("text/plain"), quant);
        RequestBody catIdBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(catId));

        ApiClient.createTiendaApiService(this).updateProduct(idBody, titleBody, descBody, priceBody, quantBody, catIdBody, imageParts).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PublishProductActivity.this, "Producto actualizado con éxito", Toast.LENGTH_SHORT).show();
                    // Redirigir a MainActivity con la pila limpia para actualizar el perfil/dashboard
                    Intent intent = new Intent(PublishProductActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(PublishProductActivity.this, "Error al actualizar el producto", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PublishProductActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
