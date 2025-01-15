package com.vlteam.vlxbookapplication;

import com.vlteam.vlxbookapplication.storage.BestScoreModel;

public class BestScore {
    private String id;
    private String date;
    private int score;
    public BestScore(SimpleDatabaseManager databaseManager, BestScoreModel model, String id){
        this.id = id;
        date = (String) databaseManager.getDataByKey(model.getTableName(), model.getSaveTime(), model.getKey(), id.toString());
        score = Integer.parseInt((String) databaseManager.getDataByKey(model.getTableName(), model.getBestScoreCol(), model.getKey(), id.toString()));
    }

    public int getScore() {
        return score;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }
}
