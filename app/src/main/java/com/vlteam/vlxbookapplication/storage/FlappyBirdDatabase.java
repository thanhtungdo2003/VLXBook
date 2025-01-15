package com.vlteam.vlxbookapplication.storage;

import android.os.Build;

import com.vlteam.vlxbookapplication.FlappyBird;
import com.vlteam.vlxbookapplication.GameCenterActivity;
import com.vlteam.vlxbookapplication.SimpleDatabaseManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FlappyBirdDatabase implements FlappyBirdStorage {
    private final SimpleDatabaseManager databaseManager;
    private static final String bestScoreTable = "best_score";
    private BestScoreModel bestScoreModel;
    public static GameCenterActivity context;

    public FlappyBirdDatabase(GameCenterActivity context) {
        databaseManager = new SimpleDatabaseManager(context);
        bestScoreModel = new BestScoreModel();
        databaseManager.createSimpleTable(bestScoreTable, bestScoreModel.getDataStruct());
        FlappyBirdDatabase.context = context;
    }

    @Override
    public SimpleDatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public void UpdateBestScore(int score) {
        Map<String, Object> datas = new HashMap<>();
        datas.put(bestScoreModel.getBestScoreCol(), score);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datas.put(bestScoreModel.getSaveTimeCol(), LocalDate.now().toString() + " - " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute());
        }
        datas.put(bestScoreModel.getKey(), UUID.randomUUID().toString());
        databaseManager.insert(bestScoreTable, datas);
    }

    @Override
    public int GetBestScore() {
        List<Object> datas = (List<Object>) databaseManager.getColumnValues(bestScoreTable, bestScoreModel.getBestScoreCol());
        int maxScore = 0;
        for (Object score : datas) {
            if ((int) score > maxScore) {
                maxScore = (int) score;
            }
        }
        return maxScore;
    }

    @Override
    public void DropData() {
        databaseManager.dropTable(bestScoreTable);
    }
}
