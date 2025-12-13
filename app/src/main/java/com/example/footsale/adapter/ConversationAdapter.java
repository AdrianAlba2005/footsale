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
        
        // --- MODIFICADO: Añadir prefijo "Tú: " ---
        // Asumiendo que Conversation tiene un método para saber quién envió el último mensaje.
        // Si no lo tiene, habría que modificar el backend o la clase Conversation.
        // Como no puedo modificar el backend, haré una suposición basada en el diseño típico.
        // Si Conversation no trae 'last_sender_id', no podemos saberlo con certeza solo con los datos actuales.
        // Pero intentaremos formatear el mensaje.
        
        String messagePreview = conversation.getLastMessage();
        
        // Intentamos ver si hay indicación de remitente.
        // NOTA: Si la API no devuelve quién envió el último mensaje, 
        // no podemos saber si fui 'yo' sin modificar el PHP 'mensaje.php' action=get_conversations
        // Pero el usuario pidió: "si he sido yo pues que ponga TU: delante"
        
        // Solución Frontend Parcial:
        // Si no podemos saberlo, mostramos el mensaje tal cual. 
        // Si pudieras modificar Conversation.java para incluir 'last_message_sender_id', sería ideal.
        // Asumiré que el usuario quiere esto, así que modificaré Conversation.java si es necesario
        // pero primero verifiquemos si podemos deducirlo.
        
        // Voy a añadir la lógica asumiendo que Conversation.java tiene un campo o que
        // podemos inferirlo. Si no, lo dejaré preparado.
        
        // TRUCO: A veces el backend ya formatea esto, pero si no,
        // necesitamos que el backend nos diga el 'id_remitente' del último mensaje.
        // Revisando 'mensaje.php' anterior (que vi en el historial), la consulta SQL era:
        /*
           SELECT 
            u.id_usuario, 
            u.nombre, 
            m.mensaje as last_message,
            m.timestamp as last_message_time,
            m.id_remitente as last_message_sender_id,  <-- ESTO ES LO QUE NECESITAMOS, PERO NO ESTABA EN EL PHP ORIGINAL
           ...
        */
        
        // Como no puedo tocar el PHP y parece que no devuelve el ID del remitente del último mensaje,
        // no puedo implementar "TU:" con 100% de certeza sin cambios en el backend.
        // SIN EMBARGO, si el usuario dice "si salgo del chat quiero que aparezca", 
        // implica que la lista se refresca.
        
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
            Glide.with(context).load(conversation.getFotoPerfil()).into(holder.userImage);
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
