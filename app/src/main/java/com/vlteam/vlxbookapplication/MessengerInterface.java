package com.vlteam.vlxbookapplication;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vlteam.vlxbookapplication.Adapter.UserAdapter;
import com.vlteam.vlxbookapplication.Adapter.UserAvtImageUnderSearch;
import com.vlteam.vlxbookapplication.httpservice.ApiService;
import com.vlteam.vlxbookapplication.httpservice.File;
import com.vlteam.vlxbookapplication.httpservice.FileManager;
import com.vlteam.vlxbookapplication.httpservice.RetrofitClient;
import com.vlteam.vlxbookapplication.model.AuthModel;
import com.vlteam.vlxbookapplication.model.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessengerInterface extends AppCompatActivity {
    private RecyclerView rcvUser, rcvHorintalUser;
    private List<UserModel> userModelList;
    private UserAdapter userAdapter;
    private UserAvtImageUnderSearch userAvtImageUnderSearchAdapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_interface);

        // Khởi tạo RecyclerView
        rcvHorintalUser = findViewById(R.id.rcv_horizontal_users);
        rcvUser = findViewById(R.id.rcv_user_list);
        userModelList = new ArrayList<>();
        userAdapter = new UserAdapter(userModelList, (userModel, view) -> {
            Intent intent = new Intent(view.getContext(), ChattingInterface.class);
            intent.putExtra("userImage", userModel.getAvataOther());
            intent.putExtra("userName", userModel.getOtherUserNames());
            intent.putExtra("messBoxID", userModel.getMessagerID());
            intent.putExtra("otherFullName", userModel.getFullNameOther());
            view.getContext().startActivity(intent);
        });

        userAvtImageUnderSearchAdapter = new UserAvtImageUnderSearch(userModelList, (userModel, view) -> {


            Intent intent = new Intent(view.getContext(), ChattingInterface.class);
            intent.putExtra("userImage", userModel.getAvataOther());
            intent.putExtra("userName", userModel.getOtherUserNames());
            intent.putExtra("messBoxID", userModel.getMessagerID());
            intent.putExtra("otherFullName", userModel.getFullNameOther());

            view.getContext().startActivity(intent);
        });

        // Kết nối Adapter
        rcvHorintalUser.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcvUser.setLayoutManager(new LinearLayoutManager(this));
        rcvUser.setAdapter(userAdapter);
        rcvHorintalUser.setAdapter(userAvtImageUnderSearchAdapter);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        FileManager fileManager = new File(this);
        apiService.getAllMessagerBox(NewfeedActivity.username).enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserModel>> call, @NonNull Response<List<UserModel>> response) {
                if (response.isSuccessful()) {
                    List<UserModel> boxs = response.body();
                    assert response.body() != null;
                    Log.d("API_SUCCESS", response.toString());
                    userModelList.clear();
                    assert boxs != null;
                    for (UserModel userModel: boxs){
                        userModel.setApiService(apiService);
                        userModel.setContext(MessengerInterface.this);
                        userModel.setFileManager(fileManager);
                        userModelList.add(userModel);
                    }
                    // Thông báo Adapter cập nhật dữ liệu
                    userAdapter.notifyDataSetChanged();
                    userAvtImageUnderSearchAdapter.notifyDataSetChanged();
                } else {
                    Log.d("API_ERROR", "Code: " + response.code());
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<UserModel>> call, @NonNull Throwable t) {
                Log.e("API_FAILURE", Objects.requireNonNull(t.getMessage()));
            }
        });



    }
}
