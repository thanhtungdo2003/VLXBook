package com.vlteam.vlxbookapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vlteam.vlxbookapplication.Adapter.FriendsProposeBarAdapter;
import com.vlteam.vlxbookapplication.Adapter.UserAdapter;
import com.vlteam.vlxbookapplication.Adapter.UserAvtImageUnderSearch;
import com.vlteam.vlxbookapplication.httpservice.ApiService;
import com.vlteam.vlxbookapplication.httpservice.File;
import com.vlteam.vlxbookapplication.httpservice.FileManager;
import com.vlteam.vlxbookapplication.httpservice.RetrofitClient;
import com.vlteam.vlxbookapplication.model.UserInfoModel;
import com.vlteam.vlxbookapplication.model.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendListFindMainActivity extends AppCompatActivity {
    private RecyclerView rcvUsersItems;
    private List<UserInfoModel> userModelList;
    private FriendsProposeBarAdapter userAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_friend_list_find_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.open_messager_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FriendListFindMainActivity.this, MessengerInterface.class));

            }
        });
        findViewById(R.id.open_home_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FriendListFindMainActivity.this, NewfeedActivity.class));
                overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);

            }
        });
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        rcvUsersItems = findViewById(R.id.recyclerViewFriendPropose);
        userModelList = new ArrayList<>();

        userAdapter = new FriendsProposeBarAdapter(userModelList, (userInfoModel, view) -> {
                if ((view.getId() == R.id.open_messager_box_fp_btn)) {
                    apiService.createBoxMess(NewfeedActivity.username, userInfoModel.UserName).enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                            if (response.isSuccessful()) {
                                UserModel box = response.body();
                                assert response.body() != null;
                                Log.d("API_SUCCESS", response.toString());
                                Intent intent = new Intent(view.getContext(), ChattingInterface.class);
                                intent.putExtra("userImage", box.getAvataOther());
                                intent.putExtra("userName", box.getOtherUserNames());
                                intent.putExtra("messBoxID", box.getMessagerID());
                                intent.putExtra("otherFullName", view.getTag().toString());
                                view.getContext().startActivity(intent);


                            } else {
                                Log.d("API_ERROR", "Code: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                            Log.e("API_FAILURE", Objects.requireNonNull(t.getMessage()));
                        }
                    });
            }else {
                Intent intent = new Intent(view.getContext(), InfoPage.class);
                intent.putExtra("other_username", userInfoModel.UserName);
                view.getContext().startActivity(intent);
            }
        });

        // Kết nối Adapter
        rcvUsersItems.setLayoutManager(new LinearLayoutManager(this));
        rcvUsersItems.setAdapter(userAdapter);
        FileManager fileManager = new File(this);
        apiService.getAllUser(1).enqueue(new Callback<List<UserInfoModel>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<UserInfoModel>> call, @NonNull Response<List<UserInfoModel>> response) {
                if (response.isSuccessful()) {
                    List<UserInfoModel> users = response.body();
                    assert response.body() != null;
                    Log.d("API_SUCCESS", response.toString());
                    userModelList.clear();
                    for (UserInfoModel info : users) {
                        if (info.UserName.equals(NewfeedActivity.username)) continue;
                        info.setApiService(apiService);
                        info.setContext(FriendListFindMainActivity.this);
                        info.setFileManager(fileManager);
                        userModelList.add(info);
                    }
                    userAdapter.notifyDataSetChanged();
                } else {
                    Log.d("API_ERROR", "Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserInfoModel>> call, @NonNull Throwable t) {
                Log.e("API_FAILURE", Objects.requireNonNull(t.getMessage()));
            }
        });

    }
}