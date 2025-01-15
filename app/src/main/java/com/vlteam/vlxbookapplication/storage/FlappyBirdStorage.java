package com.vlteam.vlxbookapplication.storage;

import com.vlteam.vlxbookapplication.SimpleDatabaseManager;

public interface FlappyBirdStorage {
    SimpleDatabaseManager getDatabaseManager();
    void UpdateBestScore(int score);
    int GetBestScore();
    void DropData();
}
