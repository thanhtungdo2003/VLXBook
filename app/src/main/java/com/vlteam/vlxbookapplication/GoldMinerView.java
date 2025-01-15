package com.vlteam.vlxbookapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.vlteam.vlxbookapplication.game.GoldMinerControlActivity;
import com.vlteam.vlxbookapplication.storage.Gold;
import com.vlteam.vlxbookapplication.storage.GoldMinerDatabase;
import com.vlteam.vlxbookapplication.storage.Ore;
import com.vlteam.vlxbookapplication.storage.Rock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GoldMinerView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private int point = 0;
    private Context adminActivity;
    private Bitmap background;
    private Bitmap miner;
    private Bitmap hook;
    private Bitmap pointer;
    private boolean isHook = false;
    private boolean isDrawBG = false;
    private boolean running = true;
    private List<Ore> ores = new ArrayList<>();
    private List<float[]> hookPath = new ArrayList<>();
    private List<float[]> cylPath = new ArrayList<>();
    private boolean waitingPeriod = false;
    private float xPointer = 40;
    private float xHook = 500;
    private float yHook = 180F;
    private int pathIndexHook = 0;
    private int pathCylIndexHook = 0;
    private boolean isHit = false;
    private Ore oreMining = null;
    private GoldMinerDatabase goldMinerDatabase;


    public GoldMinerView(Context context) {
        super(context);
        goldMinerDatabase = GoldMinerControlActivity.goldMinerDatabase;
        holder = getHolder();
        holder.addCallback(this);
        adminActivity = context;
        background = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.gold_miner_background),
                1400, 715, false
        );
        hook = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.point),
                10, 10, false
        );
        miner = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.miner),
                150, 150, false
        );
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            Gold g = new Gold(this, 100 + random.nextInt(200)).create();
            ores.add(g);
        }
        for (int i = 0; i < 5; i++) {
            Rock g = new Rock(this, 100 + random.nextInt(200)).create();
            ores.add(g);
        }
        cylPath = generateTopPartialCirclePoints(650, 80, 120, 20, 100);
        pathCylIndexHook = cylPath.size() - 1;
    }


    public void run() {
        new Thread(() -> {
            while (running) {
                if (holder.getSurface().isValid()) {
                    Canvas canvas = holder.lockCanvas();
                    if (canvas != null) {
                        drawOnCanvas(canvas);
                        waitingHook();
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
                try {
                    Thread.sleep(16); //
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void waitingHook() {
        if (isHook) return;
        if (waitingPeriod) {
            xPointer -= 65;
            if (pathCylIndexHook < cylPath.size() - 1) {
                pathCylIndexHook++;
            }
            if (xPointer <= 80) {
                waitingPeriod = false;
            }
        } else {
            xPointer += 65;

            if (pathCylIndexHook > 0) {
                pathCylIndexHook--;
            }
            if (xPointer >= 1380) {
                waitingPeriod = true;
            }
        }
    }

    private void hook() {
        isHook = true;
        hookPath = generatePath(xHook, yHook, xPointer, 700, 25);
    }

    @SuppressLint("SetTextI18n")
    private void drawOnCanvas(Canvas canvas) {
        // Vẽ nền
        canvas.drawBitmap(background, 0, -35, null);
        canvas.drawBitmap(miner, 600, 0, null);

        // Vẽ đối tượng
        for (Ore ore : ores) {
            ore.draw(canvas);
        }
        if (isHook) {
            if (!hookPath.isEmpty()) {
                if (pathIndexHook > hookPath.size() - 1) {
                    reloadHook();
                    return;
                }
                float[] location = hookPath.get(pathIndexHook);
                if (location != null) {
                    pathIndexHook++;
                    xHook = location[0];
                    yHook = location[1];
                    isHitOre();
                }
            }
        } else if (isHit) {
            if (!hookPath.isEmpty()) {
                if (pathIndexHook > hookPath.size() - 1) {
                    reloadHook();
                    isHit = false;
                    int pointAdd = 0;
                    if (oreMining instanceof Gold) {
                        pointAdd += oreMining.getPoint();
                    } else {
                        pointAdd += oreMining.getPoint() / 10;
                    }
                    point += pointAdd;
                    goldMinerDatabase.addMoney(point);
                    GoldMinerControlActivity.goalMoneyTextView.setText("$" + goldMinerDatabase.getMoney());

                    ores.remove(oreMining);
                    return;
                }
                float[] location = hookPath.get(pathIndexHook);
                if (location != null) {
                    pathIndexHook++;
                    xHook = location[0];
                    yHook = location[1];
                    oreMining.setX((int) xHook - oreMining.getImg().getWidth() / 2);
                    oreMining.setY((int) yHook - 10);
                }
            }
        } else {
            float[] locationHookWait = cylPath.get(pathCylIndexHook);
            xHook = locationHookWait[0];
            yHook = locationHookWait[1];
        }
        Paint paint = new Paint();
        Paint pointTextPaint = new Paint();
        paint.setColor(Color.rgb(60, 60, 60));
        paint.setStrokeWidth(4.5F);
        canvas.drawLine(650, 80, xHook, yHook, paint);
        canvas.drawBitmap(hook, xHook, yHook, null);

        pointTextPaint.setColor(Color.rgb(30, 200, 30));
        pointTextPaint.setTextSize(60);
        canvas.drawText("$" + point, 80, 80, pointTextPaint);
    }

    private void reloadHook() {
        hookPath.clear();
        isHook = false;
        pathIndexHook = 0;
        pathCylIndexHook = cylPath.size() - 1;
        xPointer = 40;
        xHook = 500F;
        yHook = 180F;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Surface đã sẵn sàng
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Xử lý khi Surface thay đổi
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        running = false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!isHit && !isHook) hook();
            return true;
        }
        return false;
    }

    private boolean isHitOre() {
        for (Ore o : ores) {
            if (isHookOre(o)) {
                isHook = false;
                isHit = true;
                hookPath = generatePath(xHook, yHook, 650, 80, (int) (o.getPoint() / 1.5));
                oreMining = o;
                pathIndexHook = 0;
                return true;
            }

        }
        return false;
    }

    public boolean isHookOre(Ore ore) {
        // Tính bounding box cho bitmap1
        float bitmap1Right = xHook + hook.getWidth();
        float bitmap1Bottom = yHook + hook.getHeight();

        // Tính bounding box cho bitmap2
        float bitmap2Left = ore.getX();
        float bitmap2Top = ore.getY();
        float bitmap2Right = ore.getX() + ore.getImg().getWidth();
        float bitmap2Bottom = ore.getY() + ore.getImg().getHeight();

        // Kiểm tra xem hai bounding box có giao nhau không
        return !(bitmap1Right < bitmap2Left ||  // bitmap1 bên trái bitmap2
                xHook > bitmap2Right ||  // bitmap1 bên phải bitmap2
                bitmap1Bottom < bitmap2Top ||  // bitmap1 ở trên bitmap2
                yHook > bitmap2Bottom);   // bitmap1 ở dưới bitmap2
    }

    public static List<float[]> generatePath(float startX, float startY, float endX, float endY, int steps) {
        List<float[]> coordinates = new ArrayList<>();

        // Tính khoảng cách giữa các bước
        float deltaX = (endX - startX) / steps;
        float deltaY = (endY - startY) / steps;

        // Tạo các tọa độ theo hướng từ A đến B
        for (int i = 0; i <= steps; i++) {
            float x = startX + i * deltaX;
            float y = startY + i * deltaY;
            coordinates.add(new float[]{x, y});
        }
        return coordinates;
    }

    public static List<float[]> generateTopPartialCirclePoints(float centerX, float centerY, float radius, int numPoints, float angleDegree) {
        List<float[]> points = new ArrayList<>();

        // Tính góc offset (phân bố góc 70 độ ở nửa trên)
        double angleOffset = Math.toRadians(angleDegree / 2);
        double startAngle = Math.PI / 2 - angleOffset;    // Góc bắt đầu (90° - offset)
        double endAngle = Math.PI / 2 + angleOffset;      // Góc kết thúc (90° + offset)

        // Bước chia góc
        double angleStep = (endAngle - startAngle) / (numPoints - 1);

        // Tạo tọa độ
        for (int i = 0; i < numPoints; i++) {
            double angle = startAngle + i * angleStep;
            float x = (float) (centerX + radius * Math.cos(angle));
            float y = (float) (centerY + radius * Math.sin(angle));

            points.add(new float[]{x, y});
        }

        return points;
    }


}
