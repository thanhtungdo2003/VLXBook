package com.vlteam.vlxbookapplication.storage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BestScoreModel {

    private final static String tableName = "best_score";
    private final static String bestScoreCol = "bestScore";
    private final static String key = "ID";
    private final static String saveTime = "saveTime";
    private Integer bestScore;

    public int getBestScore() {
        return bestScore;
    }

    public String getKey() {
        return key;
    }

    public String getTableName() {
        return tableName;
    }

    public String getSaveTime() {
        return saveTime;
    }

    public String getBestScoreCol() {
        return bestScoreCol;
    }

    public String getSaveTimeCol() {
        return saveTime;
    }

    public void setBestScore(Integer bestScore) {
        this.bestScore = bestScore;
    }

    public List<String> getDataStruct() {
        List<String> datas = new ArrayList<>();
        datas.add(key + " TEXT(50)");
        datas.add(saveTime + " TEXT(10)");
        datas.add(bestScoreCol + " INT");
        return datas;
    }
}
