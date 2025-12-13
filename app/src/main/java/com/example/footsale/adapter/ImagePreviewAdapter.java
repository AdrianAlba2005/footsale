package com.example.footsale.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.footsale.R;
import java.util.List;

public class ImagePreviewAdapter extends RecyclerView.Adapter<ImagePreviewAdapter.ViewHolder> {

    private final Context context;
    private final List<Uri> imageUris;
    private final OnImageRemoveListener listener;

    public interface OnImageRemoveListener {
        void onImageRemoved(int position);
    }

    public ImagePreviewAdapter(Context context, List<Uri> imageUris, OnImageRemoveListener listener) {
        this.context = context;
        this.imageUris = imageUris;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri uri = imageUris.get(position);
        Glide.with(context).load(uri).into(holder.imageView);
        holder.removeButton.setOnClickListener(v -> listener.onImageRemoved(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.previewImageView);
            removeButton = itemView.findViewById(R.id.removeImageButton);
        }
    }
}
