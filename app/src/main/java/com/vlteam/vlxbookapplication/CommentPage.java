package com.vlteam.vlxbookapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vlteam.vlxbookapplication.Adapter.CommentAdapter;
import com.vlteam.vlxbookapplication.model.CommentModel;

import java.util.ArrayList;
import java.util.List;

public class CommentPage extends AppCompatActivity {

    private RecyclerView rcvComment;
    private CommentAdapter commentAdapter;
    private List<CommentModel> commentList;
    ImageView btnBackCommentToNewfeed, btnComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comment_page);

        btnBackCommentToNewfeed = findViewById(R.id.btn_back_comment_to_newfeed);
        btnComment = findViewById(R.id.btn_comment);

        btnBackCommentToNewfeed.setOnClickListener(v -> finish());

        rcvComment = findViewById(R.id.rcv_comment);
        rcvComment.setLayoutManager(new LinearLayoutManager(this));

        // Dữ liệu mẫu
        commentList = new ArrayList<>();
        commentList.add(new CommentModel(R.drawable.avt_4, "1", "Tớ là Tùng nghu nè hihihi","Tùng đần",9));
        commentList.add(new CommentModel(R.drawable.avt_4, "2", "Tớ cũng bị ngu","Tùng dần",8));
        commentList.add(new CommentModel(R.drawable.avt_6, "2", "Phải hông dị?","Lương",7));
        commentList.add(new CommentModel(R.drawable.avt_4, "2", "Juan k cần chỉnh","Tùng dốt",6));
        commentList.add(new CommentModel(R.drawable.avt_9, "2", "Wow","Dương",5));
        commentList.add(new CommentModel(R.drawable.avt_7, "2", "Adu","Dương",4));
        commentList.add(new CommentModel(R.drawable.avt_8, "2", "Thực sự là tôi rất ấn tượng với chất lượng của sản phẩm này. Đầu tiên, tôi phải nói rằng khi nhận được hàng, tôi rất bất ngờ về độ hoàn thiện và sự tỉ mỉ của nó.","Lương",3));

        commentAdapter = new CommentAdapter(commentList);
        rcvComment.setAdapter(commentAdapter);
    }
}
