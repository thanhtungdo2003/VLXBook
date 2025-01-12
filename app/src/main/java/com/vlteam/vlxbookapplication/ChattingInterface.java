package com.vlteam.vlxbookapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vlteam.vlxbookapplication.Adapter.MessageAdapter;
import com.vlteam.vlxbookapplication.model.MessageModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChattingInterface extends AppCompatActivity {
    ImageView btnBackChat, btnCallChat, btnCallVideoChat, btnInfoChat, btnSendMessage;
    TextView tvUserNameChatting;
    CircleImageView CimgAvtUserChat;
    EditText edtMessageInput;
    RecyclerView rcvMessages;
    MessageAdapter messageAdapter;
    List<MessageModel> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_interface);

        btnBackChat = findViewById(R.id.btn_back_chatting);
        btnSendMessage = findViewById(R.id.btn_send_message);
        tvUserNameChatting = findViewById(R.id.tv_name_user_chatting);
        CimgAvtUserChat = findViewById(R.id.img_avt_user_chating);
        edtMessageInput = findViewById(R.id.edt_message_input);
        rcvMessages = findViewById(R.id.rcv_message_box);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        rcvMessages.setLayoutManager(new LinearLayoutManager(this));
        rcvMessages.setAdapter(messageAdapter);

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        int userImageResId = intent.getIntExtra("userImage", 0);

        tvUserNameChatting.setText(userName);
        CimgAvtUserChat.setImageResource(userImageResId);

        btnBackChat.setOnClickListener(v -> finish());

        btnSendMessage.setOnClickListener(v -> {
            String messageText = edtMessageInput.getText().toString().trim();
            if (!messageText.isEmpty()) {
                MessageModel newMessage = new MessageModel(messageText, "chatID", userName, "msgID", null, new Date());
                messageList.add(newMessage);
                messageAdapter.notifyItemInserted(messageList.size() - 1);
                rcvMessages.scrollToPosition(messageList.size() - 1);
                edtMessageInput.setText("");
            }
        });
    }
}