package com.example.footsale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.footsale.R;
import com.example.footsale.entidades.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.List;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.UserViewHolder> {

    public interface OnUserActionListener {
        void onBanUser(Usuario user);
        void onViewDetails(Usuario user);
    }

    private Context context;
    private List<Usuario> userList;
    private OnUserActionListener listener;

    public AdminUserAdapter(Context context, List<Usuario> userList, OnUserActionListener listener) {
        this.context = context;
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Usamos un layout simple para cada usuario. Crearemos item_usuario_admin.xml si no existe.
        View view = LayoutInflater.from(context).inflate(R.layout.item_usuario_admin, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Usuario user = userList.get(position);
        holder.userName.setText(user.getNombre());
        holder.userEmail.setText(user.getEmail());
        
        // Mostrar rol si es relevante
        String rol = user.getRol() != null ? user.getRol() : "Usuario";
        holder.userRole.setText(rol.substring(0, 1).toUpperCase() + rol.substring(1));

        if (user.getFotoPerfil() != null && !user.getFotoPerfil().isEmpty()) {
            Glide.with(context).load(user.getFotoPerfil()).placeholder(R.drawable.ic_person).into(holder.userImage);
        } else {
            holder.userImage.setImageResource(R.drawable.ic_person);
        }

        holder.btnBan.setOnClickListener(v -> listener.onBanUser(user));
        holder.itemView.setOnClickListener(v -> listener.onViewDetails(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        TextView userName, userEmail, userRole;
        Button btnBan;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            userRole = itemView.findViewById(R.id.userRole);
            btnBan = itemView.findViewById(R.id.btnBanUser);
        }
    }
}