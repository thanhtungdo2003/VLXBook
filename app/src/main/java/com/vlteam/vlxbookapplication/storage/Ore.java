package com.vlteam.vlxbookapplication.storage;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public interface Ore {
    public Ore create();
    public void draw(Canvas canvas);
    public Bitmap getImg();
    public int getX();
    public int getY();
    public void setX(int x);
    public void setY(int y);
    public int getPoint();
}
