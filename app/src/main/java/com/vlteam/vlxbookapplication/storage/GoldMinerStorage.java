package com.vlteam.vlxbookapplication.storage;

import com.vlteam.vlxbookapplication.SimpleDatabaseManager;

public interface GoldMinerStorage {
    SimpleDatabaseManager getDatabaseManager();
    void updateMoney(int money);
    int getMoney();
    void setMoney(int money);
    boolean addMoney(int money);
    boolean subtractMoney(int money);
    void DropData();
}
