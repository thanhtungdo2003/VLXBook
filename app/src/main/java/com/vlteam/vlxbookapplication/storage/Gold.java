package com.vlteam.vlxbookapplication.storage;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.vlteam.vlxbookapplication.GoldMinerView;
import com.vlteam.vlxbookapplication.R;

import java.util.Random;

public class Gold implements Ore{
    private int X = 0;
    private int Y = 0;
    private int point;
    private Bitmap img;

    public Gold(GoldMinerView view,int point) {
        img = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(view.getResources(), R.drawable.gold),
                (int) (point/2.5), (int) (point/2.5), false
        );
        this.point = point;

    }

    @Override
    public Bitmap getImg() {
        return img;
    }

    @Override
    public int getX() {
        return X;
    }

    @Override
    public int getY() {
        return Y;
    }

    @Override
    public void setX(int x) {
        this.X = x;
    }

    @Override
    public void setY(int y) {
        this.Y = y;
    }

    @Override
    public Gold create(){
        Random random = new Random();
        X = 300 + random.nextInt(600);
        Y = 340 + random.nextInt(200);
        return this;
    }

    @Override
    public void draw(Canvas canvas) {

        canvas.drawBitmap(img, X, Y, null);
    }

    @Override
    public int getPoint() {
        return point;
    }
}
