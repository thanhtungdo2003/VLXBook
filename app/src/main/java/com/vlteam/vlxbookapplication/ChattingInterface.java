package com.vlteam.vlxbookapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vlteam.vlxbookapplication.Adapter.MessageAdapter;
import com.vlteam.vlxbookapplication.httpservice.ApiService;
import com.vlteam.vlxbookapplication.httpservice.RetrofitClient;
import com.vlteam.vlxbookapplication.model.ChatMessagerSendReponse;
import com.vlteam.vlxbookapplication.model.MessageModel;
import com.vlteam.vlxbookapplication.model.UserModel;

import java.io.Console;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChattingInterface extends AppCompatActivity {
    ImageView btnBackChat, btnCallChat, btnCallVideoChat, btnInfoChat, btnSendMessage;
    TextView tvUserNameChatting;
    CircleImageView CimgAvtUserChat;
    EditText edtMessageInput;
    RecyclerView rcvMessages;
    MessageAdapter messageAdapter;
    List<MessageModel> messageList;
    boolean isActivityVisible = true;
    List<String> messageIDList = new ArrayList<>();
    public static String currentMessagerBoxID = "";
    public static String currentOtherUserName = "";
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
        currentOtherUserName = intent.getStringExtra("userName");
        String messBoxID = intent.getStringExtra("messBoxID");
        String otherFullName = intent.getStringExtra("otherFullName");
        int userImageResId = intent.getIntExtra("userImage", 0);
        currentMessagerBoxID = messBoxID;
        tvUserNameChatting.setText(otherFullName);
        CimgAvtUserChat.setImageResource(userImageResId);

        btnBackChat.setOnClickListener(v -> finish());
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        btnSendMessage.setOnClickListener(v -> {
            String messageText = edtMessageInput.getText().toString().trim();
            if (!messageText.isEmpty()) {
                edtMessageInput.setText("");

                apiService.sendMessager(NewfeedActivity.username, currentMessagerBoxID, messageText).enqueue(new Callback<ChatMessagerSendReponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ChatMessagerSendReponse> call, @NonNull Response<ChatMessagerSendReponse> response) {
                        if (response.isSuccessful()) {
                            ChatMessagerSendReponse messagers = response.body();
                            assert response.body() != null;
                            Log.d("API_SUCCESS", response.toString());
                            MessageModel newMessage = new MessageModel();
                            newMessage.ChatMessagerID = messagers.chatMessagerID;
                            newMessage.MessagerID = messagers.MessagerID;
                            newMessage.setContent(messagers.content);
                            newMessage.setUserName(NewfeedActivity.username);
                            messageList.add(newMessage);
                            messageIDList.add(newMessage.ChatMessagerID);
                            messageAdapter.notifyItemInserted(messageList.size() - 1);
                            rcvMessages.scrollToPosition(messageList.size() - 1);
                        } else {
                            Log.d("API_ERROR", "Code: " + response.code());
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ChatMessagerSendReponse> call, @NonNull Throwable t) {
                        Log.e("API_FAILURE", Objects.requireNonNull(t.getMessage()));
                    }
                });
            }
        });

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isActivityVisible){
                    this.cancel();
                }
                apiService.getChatMessagerByID(currentMessagerBoxID).enqueue(new Callback<List<MessageModel>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<MessageModel>> call, @NonNull Response<List<MessageModel>> response) {
                        if (response.isSuccessful()) {
                            List<MessageModel> messagers = response.body();
                            assert response.body() != null;
                            Log.d("API_SUCCESS", response.toString());
                            boolean isNewChat = false;
                            for (MessageModel model : messagers){
                                if (Objects.equals(model.getContent(), "")){
                                    continue;
                                }
                                if (!messageIDList.contains(model.ChatMessagerID)){
                                    messageList.add(model);
                                    messageIDList.add(model.ChatMessagerID);
                                    isNewChat = true;
                                }
                            }
                            if (isNewChat) {
                                messageAdapter.notifyItemInserted(messageList.size() - 1);
                                rcvMessages.scrollToPosition(messageList.size() - 1);
                            }
                        } else {
                            Log.d("API_ERROR", "Code: " + response.code());
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<List<MessageModel>> call, @NonNull Throwable t) {
                        Log.e("API_FAILURE", Objects.requireNonNull(t.getMessage()));
                    }
                });
            }
        },0,2000);

    }
    @Override
    protected void onPause() {
        super.onPause();
        isActivityVisible = false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        isActivityVisible = true;
    }
}