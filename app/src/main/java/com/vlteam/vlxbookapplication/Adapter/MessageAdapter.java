package com.vlteam.vlxbookapplication.Adapter;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vlteam.vlxbookapplication.NewfeedActivity;
import com.vlteam.vlxbookapplication.R;
import com.vlteam.vlxbookapplication.model.MessageModel;
import com.vlteam.vlxbookapplication.model.UserModel;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<MessageModel> messageList;
    private static final int TYPE_SENT = 1;
    private static final int TYPE_RECEIVED = 2;
    private Uri avatarOtherUri;
    private String currentUserId;

    public MessageAdapter(List<MessageModel> messageList, Uri avatarOtherUri) {
        this.messageList = messageList;
        this.avatarOtherUri = avatarOtherUri;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_send, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageModel message = messageList.get(position);
        if (message == null) return;
        holder.contentTv.setText(message.getContent());
        if (avatarOtherUri != null && holder.senderAvata != null){
            holder.senderAvata.setImageURI(avatarOtherUri);
        }
    }

    @Override
    public int getItemCount() {
        return messageList != null ? messageList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel message = messageList.get(position);
        if (message.getUserName().equals(NewfeedActivity.username)) {
            return TYPE_SENT;
        } else {
            return TYPE_RECEIVED;
        }
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView contentTv;
        private ImageView senderAvata;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTv = itemView.findViewById(R.id.text_message_received);
            senderAvata = itemView.findViewById(R.id.img_avt_user_message_received);
        }
    }
}

