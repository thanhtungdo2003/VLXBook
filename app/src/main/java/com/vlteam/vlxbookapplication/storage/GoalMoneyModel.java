package com.vlteam.vlxbookapplication.storage;

import java.util.ArrayList;
import java.util.List;

public class GoalMoneyModel {

    private final static String tableName = "goal_money_gold_miner";
    private final static String moneyCol = "money";
    private final static String key = "ID";

    public String getKey() {
        return key;
    }


    public String getMoneyCol() {
        return moneyCol;
    }


    public String getTableName() {
        return tableName;
    }


    public List<String> getDataStruct() {
        List<String> datas = new ArrayList<>();
        datas.add(key + " TEXT(50)");
        datas.add(moneyCol + " INT");
        return datas;
    }
}
