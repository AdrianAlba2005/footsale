package com.example.footsale.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.footsale.R;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.models.BanUserRequest;
import com.example.footsale.entidades.Usuario;
import com.google.android.material.button.MaterialButton;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final Context context;
    private final List<Usuario> userList;

    public UserAdapter(Context context, List<Usuario> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Usuario user = userList.get(position);
        holder.userName.setText(user.getNombre());
        holder.userEmail.setText(user.getEmail());
        
        // Cargar foto de perfil
        if (user.getFotoPerfil() != null && !user.getFotoPerfil().isEmpty()) {
            Glide.with(context)
                .load(ApiClient.getFullImageUrl(user.getFotoPerfil()))
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(holder.imgUserProfile);
        } else {
            holder.imgUserProfile.setImageResource(R.drawable.ic_person);
        }

        // Lógica botón Banear
        holder.btnBan.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                .setTitle("Banear Usuario")
                .setMessage("¿Estás seguro de que quieres banear a " + user.getNombre() + "?\nEsta acción eliminará al usuario.")
                .setPositiveButton("Sí, banear", (dialog, which) -> {
                    banUser(user.getIdUsuario(), position);
                })
                .setNegativeButton("Cancelar", null)
                .show();
        });
    }

    private void banUser(int userId, int position) {
        ApiClient.createUsuarioApiService(context).banUser(new BanUserRequest(userId)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Usuario baneado correctamente", Toast.LENGTH_SHORT).show();
                    userList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, userList.size());
                } else {
                    Toast.makeText(context, "Error al banear usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userEmail;
        CircleImageView imgUserProfile;
        MaterialButton btnBan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.tvUserName);
            userEmail = itemView.findViewById(R.id.tvUserEmail);
            imgUserProfile = itemView.findViewById(R.id.imgUserProfile);
            btnBan = itemView.findViewById(R.id.btnBan);
        }
    }
}
