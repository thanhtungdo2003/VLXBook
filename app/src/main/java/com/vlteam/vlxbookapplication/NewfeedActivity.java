package com.vlteam.vlxbookapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vlteam.vlxbookapplication.Adapter.ArticleAdapter;
import com.vlteam.vlxbookapplication.model.Article;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewfeedActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArticleAdapter postAdapter;
    private List<Article> postList;
    public static String tokenAccount = "";
    public static String username = "";
    ImageButton btnNext;
    Button btnCreateSTT;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.newfeed);


        if (username.isEmpty()){
            startActivity(new Intent(this, login.class));
            return;
        }

        btnCreateSTT = findViewById(R.id.btnCreateStatus);
        btnCreateSTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextToCreateSTTPage = new Intent(NewfeedActivity.this,createStatusPage.class);
                nextToCreateSTTPage.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                startActivity(nextToCreateSTTPage);
            }
        });
        findViewById(R.id.open_messager_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewfeedActivity.this, MessengerInterface.class));

            }
        });


        recyclerView = findViewById(R.id.recyclerViewPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Khởi tạo danh sách bài viết
        postList = new ArrayList<>();
        // Thêm một số bài viết mẫu vào danh sách
        postList.add(new Article("Tùng Ăn C", "1", "Ăn c sdvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1", "image_path", new Date(), "video_path"));
        postList.add(new Article("Tùng Ăn D", "1", "Ăn c 2", "logo.png", new Date(), "video_path"));
        postList.add(new Article("Tùng Ăn D", "1", "Ăn c 2", "image_path", new Date(), "video_path"));
        postList.add(new Article("Tùng Ăn D", "1", "Ăn c 2", "image_path", new Date(), "video_path"));
        postAdapter = new ArticleAdapter(postList);
        postAdapter.setOnItemClickListener((position,article)->{
            Toast.makeText(this, "CLicked at positon:"+position, Toast.LENGTH_SHORT).show();
        });

        recyclerView.setFocusable(false);
        recyclerView.setAdapter(postAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}