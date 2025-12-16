package com.example.footsale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.footsale.R;
import com.example.footsale.api.ApiClient;
import com.example.footsale.api.models.DeleteReviewRequest;
import com.example.footsale.entidades.Resena;
import com.example.footsale.utils.SessionManager;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResenaAdapter extends RecyclerView.Adapter<ResenaAdapter.ViewHolder> {

    private final Context context;
    private final List<Resena> resenaList;
    private final SessionManager sessionManager;

    public ResenaAdapter(Context context, List<Resena> resenaList) {
        this.context = context;
        this.resenaList = resenaList;
        this.sessionManager = new SessionManager(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_resena, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Resena resena = resenaList.get(position);
        
        // Asignar el nombre del autor
        holder.authorName.setText(resena.getNombreAutor());
        
        // Cargar la foto del autor usando Glide
        Glide.with(context)
             .load(ApiClient.getFullImageUrl(resena.getFotoAutor()))
             .placeholder(R.drawable.ic_person) // Avatar por defecto si no hay foto o falla la carga
             .error(R.drawable.ic_person)
             .circleCrop() // Recortar la imagen en círculo
             .into(holder.avatar);

        holder.comment.setText(resena.getComentario());
        holder.ratingBar.setRating(resena.getPuntuacion());

        if (sessionManager.isLoggedIn() && sessionManager.getUserId() == resena.getIdUsuarioAutor()) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(v -> {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    deleteReview(resenaList.get(currentPosition), currentPosition);
                }
            });
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    private void deleteReview(Resena resena, int position) {
        ApiClient.createTiendaApiService(context).deleteReview(new DeleteReviewRequest(resena.getId())).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    resenaList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, resenaList.size());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {}
        });
    }

    @Override
    public int getItemCount() {
        return resenaList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView authorName, comment;
        RatingBar ratingBar;
        ImageView deleteButton, avatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            authorName = itemView.findViewById(R.id.tvAuthorName);
            comment = itemView.findViewById(R.id.tvComment);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            deleteButton = itemView.findViewById(R.id.ivDeleteReview);
            avatar = itemView.findViewById(R.id.ivAvatar);
        }
    }
}
