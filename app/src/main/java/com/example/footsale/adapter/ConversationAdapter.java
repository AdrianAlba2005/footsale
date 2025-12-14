package com.example.footsale.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.footsale.ChatActivity;
import com.example.footsale.R;
import com.example.footsale.api.ApiClient;
import com.example.footsale.entidades.Conversation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    private final Context context;
    private List<Conversation> conversationList;
    private final int currentUserId;

    public ConversationAdapter(Context context, List<Conversation> conversationList, int currentUserId) {
        this.context = context;
        this.conversationList = conversationList;
        this.currentUserId = currentUserId;
    }
    
    // Método para actualizar datos sin recrear el adaptador
    public void updateData(List<Conversation> newConversations) {
        this.conversationList = newConversations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_conversation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Conversation conversation = conversationList.get(position);
        
        holder.userName.setText(conversation.getNombre());
        
        String messagePreview = conversation.getLastMessage();
        
        holder.lastMessage.setText(messagePreview);
        
        // Formatear hora (solo hora:minutos si es hoy, fecha si es otro día)
        holder.timestamp.setText(formatTime(conversation.getLastMessageTime()));

        if (conversation.getUnreadCount() > 0) {
            holder.unreadCount.setText(String.valueOf(conversation.getUnreadCount()));
            holder.unreadCount.setVisibility(View.VISIBLE);
            holder.lastMessage.setTypeface(null, android.graphics.Typeface.BOLD);
            holder.lastMessage.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            holder.unreadCount.setVisibility(View.GONE);
            holder.lastMessage.setTypeface(null, android.graphics.Typeface.NORMAL);
            holder.lastMessage.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
        }

        if (conversation.getFotoPerfil() != null && !conversation.getFotoPerfil().isEmpty()) {
            Glide.with(context)
                    .load(ApiClient.getFullImageUrl(conversation.getFotoPerfil()))
                    .placeholder(R.drawable.ic_person)
                    .into(holder.userImage);
        } else {
            holder.userImage.setImageResource(R.drawable.ic_person);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra(ChatActivity.EXTRA_OTHER_USER_ID, conversation.getUserId());
            intent.putExtra(ChatActivity.EXTRA_OTHER_USER_NAME, conversation.getNombre());
            intent.putExtra(ChatActivity.EXTRA_OTHER_USER_IMAGE, conversation.getFotoPerfil());
            context.startActivity(intent);
        });
    }

    private String formatTime(String timestamp) {
        try {
            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = dbFormat.parse(timestamp);
            Date now = new Date();
            
            SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            if (dayFormat.format(date).equals(dayFormat.format(now))) {
                return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
            } else {
                return new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(date);
            }
        } catch (ParseException e) {
            return timestamp;
        }
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        TextView userName, lastMessage, timestamp, unreadCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.user_image);
            userName = itemView.findViewById(R.id.user_name);
            lastMessage = itemView.findViewById(R.id.last_message);
            timestamp = itemView.findViewById(R.id.timestamp);
            unreadCount = itemView.findViewById(R.id.unread_count);
        }
    }
}
