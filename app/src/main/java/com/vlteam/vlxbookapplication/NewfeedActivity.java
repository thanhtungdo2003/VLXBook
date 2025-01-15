package com.vlteam.vlxbookapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vlteam.vlxbookapplication.Adapter.ArticleAdapter;
import com.vlteam.vlxbookapplication.httpservice.ApiService;
import com.vlteam.vlxbookapplication.httpservice.File;
import com.vlteam.vlxbookapplication.httpservice.FileManager;
import com.vlteam.vlxbookapplication.httpservice.RetrofitClient;
import com.vlteam.vlxbookapplication.model.Article;
import com.vlteam.vlxbookapplication.model.UserInfo;
import com.vlteam.vlxbookapplication.model.UserInfoModel;
import com.vlteam.vlxbookapplication.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewfeedActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArticleAdapter postAdapter;
    private List<Article> postList;
    public static String tokenAccount = "";
    public static String username = "";
    public static UserStorage userStorage;
    public static UserInfo userInfo;
    private ApiService apiService;
    private FileManager fileManager;
    private int currentPage = 1;
    private boolean hasMulData = false;
    ImageButton btnNext,btnGamecenter;
    Button btnCreateSTT;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.newfeed);

        userStorage = new UserStorage(this);
        username = userStorage.getUserName();

        if (username.isEmpty()){
            startActivity(new Intent(this, login.class));
            return;
        }
        apiService = RetrofitClient.getClient().create(ApiService.class);
        fileManager = new File(this);
        userInfo = new UserInfo();
        apiService.getUser(NewfeedActivity.username).enqueue(new Callback<List<UserInfoModel>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NonNull Call<List<UserInfoModel>> call, @NonNull Response<List<UserInfoModel>> response) {
                if (response.isSuccessful()) {
                    UserInfoModel user = response.body().get(0);

                    userInfo.UserName = user.UserName;
                    userInfo.Name = user.Name;
                    userInfo.Surname = user.Surname;
                    userInfo.Location = user.Location;
                    userInfo.Job = user.Job;
                    userInfo.UserInfoID = user.UserInfoID;
                    userInfo.Phone = user.Phone;

                    Call<ResponseBody> avataCall = apiService.downloadAvatar(user.Avata);
                    avataCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                boolean isSaved = fileManager.saveFileToInternalStorage(NewfeedActivity.this, response.body(), user.Avata);
                                if (isSaved) {
                                    Uri fileUri = fileManager.getFileUri(NewfeedActivity.this, user.Avata);
                                    userInfo.Avata = fileUri;
                                    ((ImageView) findViewById(R.id.img_avata_create_status)).setImageURI(fileUri);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            // Xử lý lỗi kết nối hoặc ngoại lệ
                        }
                    });
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<UserInfoModel>> call, @NonNull Throwable t) {
                Log.e("API_FAILURE", Objects.requireNonNull(t.getMessage()));
            }
        });


        btnCreateSTT = findViewById(R.id.btnCreateStatus);
        btnCreateSTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextToCreateSTTPage = new Intent(NewfeedActivity.this,createStatusPage.class);
                nextToCreateSTTPage.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                nextToCreateSTTPage.putExtra("username", username);
                nextToCreateSTTPage.putExtra("avataUri", userInfo.Avata);
                startActivity(nextToCreateSTTPage);
            }
        });
        btnGamecenter = findViewById(R.id.imgBtnGamecenter);
        btnGamecenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextToGameCenter = new Intent(NewfeedActivity.this,GameCenterActivity.class);
                nextToGameCenter.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(nextToGameCenter);
            }
        });

        findViewById(R.id.open_messager_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewfeedActivity.this, MessengerInterface.class));

            }
        }); findViewById(R.id.open_friend_find_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewfeedActivity.this, FriendListFindMainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
         findViewById(R.id.img_avata_create_status).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewfeedActivity.this, InfoPage.class);
                intent.putExtra("other_username", username);
                startActivity(intent);
            }
        });


        findViewById(R.id.open_update_info_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewfeedActivity.this, update_infouser_pageActivity.class));
            }
        });
        recyclerView = findViewById(R.id.recyclerViewFriendPropose);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Khởi tạo danh sách bài viết
        postList = new ArrayList<>();

        apiService = RetrofitClient.getClient().create(ApiService.class);
        fileManager = new File(this);

        loadArticle(currentPage);
        postAdapter = new ArticleAdapter(postList);

        postAdapter.setOnItemClickListener(new ArticleAdapter.OnItemClickListener() {
            @Override
            public void Onclick(int position, Article article) {
                Toast.makeText(NewfeedActivity.this, "Clicked at position: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnCommentClick(int position, Article article) {
                Toast.makeText(NewfeedActivity.this, "Comment clicked at position: " + position, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(NewfeedActivity.this, CommentPage.class);
                intent.putExtra("UserName", article.UserName);
                intent.putExtra("ArticleID", article.ArticleID);
                intent.putExtra("Caption", article.Caption);
                intent.putExtra("Images", article.Images);
                intent.putExtra("TimeOfPost", article.TimeOfPost);
                intent.putExtra("Videos", article.Videos);
                intent.putExtra("imgUri", article.imgUris.get(article.getImgArray()[0]));
                startActivity(intent);
            }
        });

        recyclerView.setFocusable(false);
        recyclerView.setAdapter(postAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Kiểm tra nếu đã cuộn đến cuối
                if (!v.canScrollVertically(1)) { // 1 = kiểm tra hướng xuống, -1 = hướng lên
                    if (hasMulData) {
                        loadArticle(++currentPage);
                    }
                }
            }
        });
    }
    public void loadArticle(int page){
        apiService.getArticleByPage(page).enqueue(new Callback<List<Article>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<Article>> call, @NonNull Response<List<Article>> response) {
                if (response.isSuccessful()) {
                    List<Article> articles = response.body();
                    assert response.body() != null;
                    Log.d("API_SUCCESS", response.toString());
                    assert articles != null;
                    if (articles.size() > 2){
                        hasMulData = true;
                    } else if (articles.isEmpty()) {
                        hasMulData = false;
                        currentPage--;
                    }
                    for (Article article : articles){
                        article.setUp(apiService, NewfeedActivity.this, fileManager);
                        postList.add(article);
                    }
                    postAdapter.notifyDataSetChanged();
                } else {
                    Log.d("API_ERROR", "Code: " + response.code());
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Article>> call, @NonNull Throwable t) {
                Log.e("API_FAILURE", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}