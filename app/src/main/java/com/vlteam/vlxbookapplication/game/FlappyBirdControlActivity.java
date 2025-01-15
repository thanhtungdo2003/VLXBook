package com.vlteam.vlxbookapplication.game;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vlteam.vlxbookapplication.BestScore;
import com.vlteam.vlxbookapplication.FlappyBird;
import com.vlteam.vlxbookapplication.R;
import com.vlteam.vlxbookapplication.SimpleDatabaseManager;
import com.vlteam.vlxbookapplication.storage.BestScoreModel;
import com.vlteam.vlxbookapplication.storage.FlappyBirdDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FlappyBirdControlActivity extends AppCompatActivity {

    private SimpleDatabaseManager databaseManager;
    private HashMap<String, BestScore> scoreDatas = new HashMap<>();
    private List<TextView> dataRows = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_flappy_bird_control);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.join_caro_game), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            databaseManager = new SimpleDatabaseManager(FlappyBirdDatabase.context);
            BestScoreModel model = new BestScoreModel();
            for (Object id : databaseManager.getColumnValues(model.getTableName(), model.getKey())) {
                scoreDatas.put(id.toString(), new BestScore(databaseManager, model, id.toString()));
            }
            loadScore(true, 25);
            String[] selectsSort = new String[2];
            selectsSort[0] = "Giảm dần";
            selectsSort[1] = "Tăng dần";
            Spinner selectSortView = findViewById(R.id.sort_select);
            selectSortView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            loadScore(true, -1);
                            break;
                        case 1:
                            loadScore(false, -1);
                            break;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, selectsSort);
            selectSortView.setAdapter(arrayAdapter);


            findViewById(R.id.playFlappy).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent caculatorPage = new Intent(FlappyBirdControlActivity.this, FlappyBird.class);
                    startActivity(caculatorPage);
                }
            });
            return insets;
        });
    }

    private void loadScore(boolean sortType, int amount) {
        LinearLayout linearLayout = findViewById(R.id.history_list_layout);
        HashMap<Integer, String> top_infos = new HashMap<>();
        HashMap<String, BestScore> topList = new HashMap<>(scoreDatas);
        for (TextView textView : dataRows) {
            linearLayout.removeView(textView);
        }
        dataRows.clear();
        if (sortType) {
            int sizeData = 0;
            if (amount == -1){
                sizeData = scoreDatas.size();
            }else {
                sizeData = amount;
            }
            for (int i = 0; i < sizeData; i++) {
                if (topList.isEmpty()) break;
                int currentMax = -1;
                String currentId = "";

                for (String id : topList.keySet()) {
                    int score = Objects.requireNonNull(topList.get(id)).getScore();
                    if (score > currentMax) {
                        currentMax = score;
                        currentId = id;
                    }
                }
                if (topList.get(currentId) != null) {
                    top_infos.put(i, "-  " + Objects.requireNonNull(topList.get(currentId)).getDate() + "\t\t\t\t\t\t\t\t\t\t" + currentMax);
                }
                topList.remove(currentId);
            }
        } else {
            int sizeData;
            if (amount == -1){
                sizeData = scoreDatas.size();
            }else {
                sizeData = amount;
            }
            for (int i = 0; i < sizeData; i++) {
                if (topList.isEmpty()) break;
                int currentMax = Integer.MAX_VALUE;
                String currentId = "";
                for (String id : topList.keySet()) {
                    int score = Objects.requireNonNull(topList.get(id)).getScore();
                    if (score < currentMax) {
                        currentMax = score;
                        currentId = id;
                    }
                }
                if (topList.get(currentId) != null) {
                    top_infos.put(i, "-  " + Objects.requireNonNull(topList.get(currentId)).getDate() + "\t\t\t\t\t\t\t\t\t\t" + currentMax);
                }
                topList.remove(currentId);
            }
        }
        for (int i = 0; i < top_infos.size(); i++) {
            TextView button = new TextView(this);
            button.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, // Chiều rộng
                    LinearLayout.LayoutParams.WRAP_CONTENT  // Chiều cao
            ));
            button.setTextColor(Color.rgb(200, 200, 200));
            button.setText(top_infos.get(i));
            linearLayout.addView(button);
            dataRows.add(button);
        }
    }
}