package com.vlteam.vlxbookapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vlteam.vlxbookapplication.game.ControlCaroGameActivity;
import com.vlteam.vlxbookapplication.game.FlappyBirdControlActivity;
import com.vlteam.vlxbookapplication.game.GoldMinerControlActivity;

public class GameCenterActivity extends AppCompatActivity {
    public static GameCenterActivity gameCenterActivity;
    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.game_center_page);
        btnBack = findViewById(R.id.btnBackGameCTToNewfeed);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gameCenterActivity = this;
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.join_caro_game), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.join_goldminner_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(gameCenterActivity, GoldMinerControlActivity.class));
            }
        });
        findViewById(R.id.join_flappybird_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(gameCenterActivity, FlappyBirdControlActivity.class));
            }
        });
        findViewById(R.id.join_caro_gamecneter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(gameCenterActivity, ControlCaroGameActivity.class));
            }
        });
    }
}