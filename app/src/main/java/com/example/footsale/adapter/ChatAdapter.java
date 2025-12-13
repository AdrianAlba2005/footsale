package com.example.footsale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footsale.R;
import com.example.footsale.adapter.model.ChatItem;
import com.example.footsale.adapter.model.DateItem;
import com.example.footsale.adapter.model.MessageItem;
import com.example.footsale.entidades.Mensaje;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_DATE = 0;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private final List<ChatItem> chatItems;
    private final int currentUserId;

    public ChatAdapter(Context context, List<ChatItem> chatItems, int currentUserId) {
        this.chatItems = chatItems;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        ChatItem item = chatItems.get(position);
        if (item.getType() == ChatItem.TYPE_DATE) {
            return VIEW_TYPE_DATE;
        } else {
            MessageItem messageItem = (MessageItem) item;
            if (messageItem.getMensaje().getIdEmisor() == currentUserId) {
                return VIEW_TYPE_MESSAGE_SENT;
            } else {
                return VIEW_TYPE_MESSAGE_RECEIVED;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_TYPE_MESSAGE_SENT:
                return new SentMessageViewHolder(inflater.inflate(R.layout.message_item_sent, parent, false));
            case VIEW_TYPE_MESSAGE_RECEIVED:
                return new ReceivedMessageViewHolder(inflater.inflate(R.layout.message_item_received, parent, false));
            case VIEW_TYPE_DATE:
            default:
                return new DateViewHolder(inflater.inflate(R.layout.date_separator_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageViewHolder) holder).bind(((MessageItem) chatItems.get(position)).getMensaje());
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageViewHolder) holder).bind(((MessageItem) chatItems.get(position)).getMensaje());
                break;
            case VIEW_TYPE_DATE:
                ((DateViewHolder) holder).bind(((DateItem) chatItems.get(position)).getDate());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return chatItems.size();
    }

    // --- ViewHolder para mensajes enviados (con estado) ---
    private static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, productNameText;
        ImageView statusImage;

        SentMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            statusImage = itemView.findViewById(R.id.image_message_status);
            productNameText = itemView.findViewById(R.id.text_product_name);
        }

        void bind(Mensaje mensaje) {
            messageText.setText(mensaje.getContenido());
            timeText.setText(formatTime(mensaje.getFechaEnvio()));

            if (mensaje.getNombreProducto() != null && !mensaje.getNombreProducto().isEmpty()) {
                productNameText.setText(mensaje.getNombreProducto());
                productNameText.setVisibility(View.VISIBLE);
            } else {
                productNameText.setVisibility(View.GONE);
            }
            
            statusImage.setVisibility(View.VISIBLE);
            if (mensaje.isRead()) {
                statusImage.setImageResource(R.drawable.ic_done_all_blue);
            } else {
                // Si no está leído, asumimos que al menos fue recibido (doble check gris)
                statusImage.setImageResource(R.drawable.ic_done_all_grey);
            }
        }

        private String formatTime(String dbTimestamp) {
            try {
                SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                Date date = dbFormat.parse(dbTimestamp);
                return date != null ? timeFormat.format(date) : "";
            } catch (ParseException e) {
                return "";
            }
        }
    }

    // --- ViewHolder para mensajes recibidos (sin estado) ---
    private static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, productNameText;

        ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            productNameText = itemView.findViewById(R.id.text_product_name);
        }

        void bind(Mensaje mensaje) {
            messageText.setText(mensaje.getContenido());
            timeText.setText(formatTime(mensaje.getFechaEnvio()));

            if (mensaje.getNombreProducto() != null && !mensaje.getNombreProducto().isEmpty()) {
                productNameText.setText(mensaje.getNombreProducto());
                productNameText.setVisibility(View.VISIBLE);
            } else {
                productNameText.setVisibility(View.GONE);
            }
        }

        private String formatTime(String dbTimestamp) {
            try {
                SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                Date date = dbFormat.parse(dbTimestamp);
                return date != null ? timeFormat.format(date) : "";
            } catch (ParseException e) {
                return "";
            }
        }
    }

    // --- ViewHolder para la fecha ---
    private static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;

        DateViewHolder(View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.dateTextView);
        }

        void bind(String date) {
            dateText.setText(date);
        }
    }
}
