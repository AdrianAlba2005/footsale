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
import com.example.footsale.entidades.Message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    // Nueva constante para el separador de fechas
    private static final int VIEW_TYPE_DATE_HEADER = 3;

    private final Context context;
    private final List<Message> messageList;
    private final int currentUserId;

    public MessageAdapter(Context context, List<Message> messageList, int currentUserId) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        // Lógica para determinar si es un mensaje o un separador de fecha.
        // Por simplicidad en esta implementación, asumiremos que messageList ya contiene los mensajes.
        // Si quisieras insertar cabeceras de fecha dinámicamente, necesitarías una lista heterogénea.
        // Para no cambiar toda la estructura de messageList, haremos la lógica dentro de onBindViewHolder
        // o modificaremos cómo se muestran los datos.
        
        // SIN EMBARGO, el usuario pide que haya "distinción por día".
        // Lo más sencillo sin cambiar la estructura de la lista es mostrar un TextView de fecha VISIBLE
        // solo en el primer mensaje de cada día.
        
        Message message = messageList.get(position);
        if (message.getSenderId() == currentUserId) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        
        // Lógica para mostrar cabecera de fecha
        boolean showDateHeader = false;
        if (position == 0) {
            showDateHeader = true;
        } else {
            Message previousMessage = messageList.get(position - 1);
            if (!isSameDay(message.getTimestamp(), previousMessage.getTimestamp())) {
                showDateHeader = true;
            }
        }

        if (holder.getItemViewType() == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).bind(message, showDateHeader);
        } else {
            ((ReceivedMessageViewHolder) holder).bind(message, showDateHeader);
        }
    }
    
    private boolean isSameDay(String timestamp1, String timestamp2) {
        try {
            // Asumiendo formato "yyyy-MM-dd HH:mm:ss" que viene de MySQL
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(timestamp1);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(timestamp2);
            return fmt.format(date1).equals(fmt.format(date2));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // ViewHolder para mensajes enviados
    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, dateHeader;
        ImageView readStatus;

        SentMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            dateHeader = itemView.findViewById(R.id.text_date_header);
            readStatus = itemView.findViewById(R.id.text_message_status); 
        }

        void bind(Message message, boolean showDateHeader) {
            messageText.setText(message.getMessage());
            timeText.setText(formatTime(message.getTimestamp()));
            
            if (showDateHeader) {
                dateHeader.setVisibility(View.VISIBLE);
                dateHeader.setText(formatDateHeader(message.getTimestamp()));
            } else {
                dateHeader.setVisibility(View.GONE);
            }
            
            // Lógica de estado de lectura si es necesario
             if (readStatus != null) {
                 // readStatus.setVisibility(message.isRead() ? View.VISIBLE : View.GONE);
             }
        }
    }

    // ViewHolder para mensajes recibidos
    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, dateHeader;

        ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            dateHeader = itemView.findViewById(R.id.text_date_header);
        }

        void bind(Message message, boolean showDateHeader) {
            messageText.setText(message.getMessage());
            timeText.setText(formatTime(message.getTimestamp()));
            
            if (showDateHeader) {
                dateHeader.setVisibility(View.VISIBLE);
                dateHeader.setText(formatDateHeader(message.getTimestamp()));
            } else {
                dateHeader.setVisibility(View.GONE);
            }
        }
    }
    
    private static String formatTime(String timestamp) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = inputFormat.parse(timestamp);
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return outputFormat.format(date);
        } catch (ParseException e) {
            return "";
        }
    }
    
    private static String formatDateHeader(String timestamp) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = inputFormat.parse(timestamp);
            // Formato pedido: dd.mm.aaaa (que es dd.MM.yyyy en Java)
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            return outputFormat.format(date);
        } catch (ParseException e) {
            return timestamp;
        }
    }
}
