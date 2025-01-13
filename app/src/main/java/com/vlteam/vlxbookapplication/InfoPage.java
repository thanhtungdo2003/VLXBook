package com.vlteam.vlxbookapplication;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoPage extends AppCompatActivity {

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
        //Lấy LinearLayout chính
        LinearLayout infoMain = findViewById(R.id.info_main);

        findViewById(R.id.open_messager_infopage_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        //
        LinearLayout info_intro = new LinearLayout(this);
        infoMain.addView(info_intro);
        info_intro.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        info_intro.setOrientation(LinearLayout.VERTICAL);
        TextView introTitle = new TextView(this);
        introTitle.setText("Chi tiết");
        introTitle.setTextSize(17);
        info_intro.addView(introTitle);
        for (int i = 0; i < 5; i++) {
            TextView detail_item = new TextView(this);
            detail_item.setText("Sống tại: Mỹ");
            detail_item.setTextSize(16);
            detail_item.setPadding(0,13,0,0);
            info_intro.addView(detail_item);
        }
        //Tạo khung chứa bạn bè
        LinearLayout info_friends = new LinearLayout(this);
        info_friends.setOrientation(LinearLayout.VERTICAL);
        info_friends.setPadding(0,30,0,0);
        infoMain.addView(info_friends);
        info_friends.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        TextView friendsTitle = new TextView(this);
        //
        friendsTitle.setText("Bạn bè");
        friendsTitle.setTextSize(17);
        info_friends.addView(friendsTitle);
        int count = 0;
        for (int i = 0; i < 2; i++) {
            LinearLayout friendBar = new LinearLayout(this);
            info_friends.addView(friendBar);

            friendBar.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < 3; j++) {
                count++;
                LinearLayout friendContainer = new LinearLayout(this);
                friendBar.addView(friendContainer);
                friendContainer.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        1
                ));
                friendContainer.setPadding(5,5,5,5);
                friendContainer.setOrientation(LinearLayout.VERTICAL);
                //
                Button test = new Button(this);
                friendContainer.addView(test);
                test.setWidth(200);
                test.setHeight(200);
                test.setBackgroundColor(Color.rgb(245,245,245));
                test.setText(count+"");
            }
        }
    }
}