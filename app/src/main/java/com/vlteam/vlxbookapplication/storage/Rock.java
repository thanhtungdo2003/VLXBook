package com.vlteam.vlxbookapplication.storage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.vlteam.vlxbookapplication.GoldMinerView;
import com.vlteam.vlxbookapplication.R;

import java.util.Random;

public class Rock implements Ore{
    private int X = 0;
    private int Y = 0;
    private int point;
    private Bitmap img;

    public Rock(GoldMinerView view,int point) {
        img = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(view.getResources(), R.drawable.rock),
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
    public Rock create(){
        Random random = new Random();
        X = 100 + random.nextInt(990);
        Y = 320 + random.nextInt(200);
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
