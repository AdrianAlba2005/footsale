package com.example.footsale.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.footsale.R;
import com.example.footsale.entidades.Resena;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private final List<Resena> reviewList;

    public ReviewAdapter(List<Resena> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resena, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Resena review = reviewList.get(position);
        holder.author.setText(review.getNombreAutor());
        holder.comment.setText(review.getComentario());
        holder.rating.setRating(review.getPuntuacion());
        // Ajustamos la referencia al método getFechaResena()
        if (review.getFechaResena() != null) {
            holder.date.setText(review.getFechaResena());
        }
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView author, comment, date;
        RatingBar rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ajustamos los IDs para que coincidan con item_resena.xml
            author = itemView.findViewById(R.id.tvAuthorName);
            comment = itemView.findViewById(R.id.tvComment);
            // Si quieres mostrar la fecha, necesitas añadir un TextView con id tvDate en item_resena.xml
            // Si no, puedes comentar/eliminar esto
            // date = itemView.findViewById(R.id.tvDate);
            rating = itemView.findViewById(R.id.ratingBar);
        }
    }
}