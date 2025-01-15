package com.vlteam.vlxbookapplication.game;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vlteam.vlxbookapplication.GoldMinerView;

public class GoldMinerActivity extends AppCompatActivity {
    private GoldMinerView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo GameViews
        gameView = new GoldMinerView(this);

        // Đặt GameViews làm view chính
        setContentView(gameView);

        // Cấu hình UI để hỗ trợ edge-to-edge (nếu cần)
        ViewCompat.setOnApplyWindowInsetsListener(gameView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Bắt đầu game loop
        gameView.run();
    }
}