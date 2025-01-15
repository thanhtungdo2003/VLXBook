package com.vlteam.vlxbookapplication.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vlteam.vlxbookapplication.GameCenterActivity;
import com.vlteam.vlxbookapplication.R;
import com.vlteam.vlxbookapplication.storage.GoldMinerDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GoldMinerControlActivity extends AppCompatActivity {
    public static int tnt = 0;
    public static GoldMinerDatabase goldMinerDatabase;
    @SuppressLint("StaticFieldLeak")
    public static TextView goalMoneyTextView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goldMinerDatabase = new GoldMinerDatabase(GameCenterActivity.gameCenterActivity);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gold_miner_control);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.join_caro_game), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        goalMoneyTextView = ((TextView) findViewById(R.id.goal_money_text_view));
        goalMoneyTextView.setText("$" + goldMinerDatabase.getMoney());

        findViewById(R.id.join_game_gold_miner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GoldMinerControlActivity.this, GoldMinerActivity.class));
            }
        });
        findViewById(R.id.tnt_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTNT(GoldMinerControlActivity.this, new float[]{90, 450});
            }
        });
    }
    public void getTNT(Context context, float[] location){
        tnt++;
        List<float[]> endLoc = generatePath(location[0], location[1], 960,0, 50);
        Timer timer = new Timer();
        Bitmap img = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.tnt),
                50, 150, false
        );
        ImageView tntImg = new ImageView(context);
        tntImg.setImageBitmap(img);
        ConstraintLayout main = findViewById(R.id.join_caro_game);
        main.addView(tntImg);
        timer.schedule(new TimerTask() {
            int index = 0;
            @Override
            public void run() {
                float[] nextLoc = endLoc.get(index);
                tntImg.setX(nextLoc[0]);
                tntImg.setY(nextLoc[1]);
                tntImg.setMaxWidth(tntImg.getWidth()-5);
                tntImg.setMaxHeight(tntImg.getHeight()-5);
                tntImg.setRotation(tntImg.getRotation() - 10);
                index++;
                if (index >= endLoc.size()-1)this.cancel();
            }
        },0,10);
    }
    public static List<float[]> generatePath(float startX, float startY, float endX, float endY, int steps) {
        List<float[]> coordinates = new ArrayList<>();

        float deltaX = (endX - startX) / steps;
        float deltaY = (endY - startY) / steps;

        for (int i = 0; i <= steps; i++) {
            float x = startX + i * deltaX;
            float y = startY + i * deltaY;
            coordinates.add(new float[]{x, y});
        }
        return coordinates;
    }
}