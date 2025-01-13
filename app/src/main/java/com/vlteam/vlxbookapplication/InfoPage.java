package com.vlteam.vlxbookapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vlteam.vlxbookapplication.httpservice.ApiService;
import com.vlteam.vlxbookapplication.httpservice.RetrofitClient;
import com.vlteam.vlxbookapplication.model.AccountModel;
import com.vlteam.vlxbookapplication.model.ChatMessagerSendReponse;
import com.vlteam.vlxbookapplication.model.MessageModel;
import com.vlteam.vlxbookapplication.model.UserInfoModel;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoPage extends AppCompatActivity {
    private String currentUserName = "";

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_info_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        //Lấy LinearLayout chính
        LinearLayout infoMain = findViewById(R.id.info_main);
        findViewById(R.id.back_btn).setOnClickListener(v -> finish());

        Intent intent = getIntent();
        currentUserName = intent.getStringExtra("other_username");

        apiService.getUser(currentUserName).enqueue(new Callback<List<UserInfoModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserInfoModel>> call, @NonNull Response<List<UserInfoModel>> response) {
                if (response.isSuccessful()) {
                    UserInfoModel user = response.body().get(0);
                    Log.d("API_SUCCESS", response.toString());
                    ((TextView) findViewById(R.id.fullName_tv_info)).setText(user.getFullName());

                    //
                    LinearLayout info_intro = new LinearLayout(InfoPage.this);
                    infoMain.addView(info_intro);
                    info_intro.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    ));
                    info_intro.setOrientation(LinearLayout.VERTICAL);
                    TextView introTitle = new TextView(InfoPage.this);
                    introTitle.setText("Chi tiết");
                    introTitle.setTextSize(17);
                    info_intro.addView(introTitle);

                    info_intro.addView(user.getTextView(InfoPage.this, "location"));
                    info_intro.addView(user.getTextView(InfoPage.this, "job"));
                    info_intro.addView(user.getTextView(InfoPage.this, "phone"));
                    info_intro.addView(user.getTextView(InfoPage.this, "birth"));


                    //Tạo khung chứa bạn bè
                    LinearLayout info_friends = new LinearLayout(InfoPage.this);
                    info_friends.setOrientation(LinearLayout.VERTICAL);
                    info_friends.setPadding(0, 30, 0, 0);
                    infoMain.addView(info_friends);
                    info_friends.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    ));
                    TextView friendsTitle = new TextView(InfoPage.this);
                    //
                    friendsTitle.setText("Bạn bè");
                    friendsTitle.setTextSize(17);
                    info_friends.addView(friendsTitle);
                    int count = 0;
                    for (int i = 0; i < 2; i++) {
                        LinearLayout friendBar = new LinearLayout(InfoPage.this);
                        info_friends.addView(friendBar);

                        friendBar.setOrientation(LinearLayout.HORIZONTAL);
                        for (int j = 0; j < 3; j++) {
                            count++;
                            LinearLayout friendContainer = new LinearLayout(InfoPage.this);
                            friendBar.addView(friendContainer);
                            friendContainer.setLayoutParams(new LinearLayout.LayoutParams(
                                    0,
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    1
                            ));
                            friendContainer.setPadding(5, 5, 5, 5);
                            friendContainer.setOrientation(LinearLayout.VERTICAL);
                            //
                            Button test = new Button(InfoPage.this);
                            friendContainer.addView(test);
                            test.setWidth(200);
                            test.setHeight(200);
                            test.setBackgroundColor(Color.rgb(245, 245, 245));
                            test.setText(count + "");
                        }
                    }
                } else {
                    Log.d("API_ERROR", "Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserInfoModel>> call, @NonNull Throwable t) {
                Log.e("API_FAILURE", Objects.requireNonNull(t.getMessage()));
            }
        });
        findViewById(R.id.open_messager_infopage_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


    }
}