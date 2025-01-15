package com.vlteam.vlxbookapplication.storage;

import com.vlteam.vlxbookapplication.GameCenterActivity;
import com.vlteam.vlxbookapplication.SimpleDatabaseManager;

import java.util.HashMap;
import java.util.Map;

public class GoldMinerDatabase implements GoldMinerStorage {
    private final SimpleDatabaseManager databaseManager;
    private static final String goal_money_tabel = "goal_money_gold_miner";
    private final GoalMoneyModel goalMoneyModel;
    public static GameCenterActivity context;

    public GoldMinerDatabase(GameCenterActivity context) {
        databaseManager = new SimpleDatabaseManager(context);
        goalMoneyModel = new GoalMoneyModel();
        databaseManager.createSimpleTable(goal_money_tabel, goalMoneyModel.getDataStruct());
        GoldMinerDatabase.context = context;
        if (databaseManager.getColumnValues(goal_money_tabel, goalMoneyModel.getKey()).isEmpty()) {
            Map<String, Object> datas = new HashMap<>();
            datas.put(goalMoneyModel.getMoneyCol(), 0);
            datas.put(goalMoneyModel.getKey(), "player");
            databaseManager.insert(goal_money_tabel, datas);
        }
    }

    @Override
    public SimpleDatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public void updateMoney(int score) {
        Map<String, Object> datas = new HashMap<>();
        datas.put(goalMoneyModel.getMoneyCol(), score);
        datas.put(goalMoneyModel.getKey(), "player");
        databaseManager.insert(goal_money_tabel, datas);
    }


    @Override
    public int getMoney() {
        Object data = databaseManager.getDataByKey(goal_money_tabel, goalMoneyModel.getMoneyCol(), goalMoneyModel.getKey(), "player");
        if (data != null) {
            return Integer.parseInt(data.toString());
        }
        return 0;
    }

    @Override
    public void setMoney(int money) {
        databaseManager.updateDataByColumn(goal_money_tabel, goalMoneyModel.getMoneyCol(), money, goalMoneyModel.getKey(), "player");
    }

    @Override
    public boolean addMoney(int money) {
        databaseManager.updateDataByColumn(goal_money_tabel, goalMoneyModel.getMoneyCol(), getMoney() + money, goalMoneyModel.getKey(), "player");
        return true;
    }

    @Override
    public boolean subtractMoney(int money) {
        if (getMoney() <= 0) {
            return false;
        }
        databaseManager.updateDataByColumn(goal_money_tabel, goalMoneyModel.getMoneyCol(), getMoney() - money, goalMoneyModel.getKey(), "player");
        return true;
    }

    @Override
    public void DropData() {
        databaseManager.dropTable(goal_money_tabel);
    }
}
