package com.vlteam.vlxbookapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.vlteam.vlxbookapplication.game.FlappyBirdControlActivity;
import com.vlteam.vlxbookapplication.storage.FlappyBirdDatabase;
import com.vlteam.vlxbookapplication.storage.FlappyBirdStorage;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameViews extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private boolean running = true;
    private SimpleDatabaseManager databaseManager;
    private FlappyBirdStorage flappyBirdStorage;
    private Bitmap bird;
    private Bitmap background;
    private Bitmap move_background;
    private Bitmap pipe_top;
    private Bitmap pipe_bottom;
    private MediaPlayer flapSound;
    private MediaPlayer pointSound;
    private MediaPlayer hitSound;
    private int point = 0;
    private int X = 180;
    private int Y = 400;
    private double speed = 10;
    private double velocity = 73;
    private boolean isFly = false;
    private float rotationAngle = 0; // Góc xoay tính bằng độ
    private int[] pipeSpawnLocation = new int[]{680, 800};
    private Context flappyBirdControlActivityContext;
    private int bestScore = 0;
    private int X_bg = -200;
    private boolean isDead = false;
    private Timer timerFly;

    public GameViews(Context context) {
        super(context);
        flappyBirdControlActivityContext = context;
        flappyBirdStorage = new FlappyBirdDatabase(GameCenterActivity.gameCenterActivity);
        bestScore = flappyBirdStorage.GetBestScore();
        holder = getHolder();
        holder.addCallback(this);
        bird = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.flappy_bird),
                160, 160,
                false
        );
        move_background = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.flappybird_world),
                2340, 1370,
                false
        );
        pipe_bottom = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.flappybird_pipe_bottom),
                200, 1370,
                false
        );
        pipe_top = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.flappybird_pipe_top),
                200, 1370,
                false
        );
        background = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.bg_flappybird),
                740, 1370,
                false
        );
        flapSound = MediaPlayer.create(context, R.raw.flap);
        hitSound = MediaPlayer.create(context, R.raw.hit);
        pointSound = MediaPlayer.create(context, R.raw.point);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                this.cancel();
                point++;
                pointSound.start();
            }
        }, 2700);
    }

    private void drop() {
        Y += (int) speed;

        if (speed < 60D) {
            speed += 1.6D;

        }
        rotationAngle += 5;
        if (rotationAngle >= 40) {
            rotationAngle = 40;
        }
        if (Y > getHeight() || Y < -20) {
            isDead = true;
            flappyBirdStorage.UpdateBestScore(point);
            Intent mainPage = new Intent(flappyBirdControlActivityContext, FlappyBirdControlActivity.class);
            flappyBirdControlActivityContext.startActivity(mainPage);
        }
    }

    public void onFly() {
        if (timerFly != null){
            isFly = false;
            velocity = 73;
            timerFly.cancel();
        }
        isFly = true;
        speed = 20;
        Timer timer = new Timer();
        timerFly = timer;
        timer.schedule(new TimerTask() {
            int count = 0;

            @Override
            public void run() {
                count++;
                if (count > 1) {
                    isFly = false;
                    velocity = 73;
                    this.cancel();
                }
            }
        }, 0, 400);
    }

    private void fly() {
        Y -= (int) velocity;
        velocity -= 10;
        rotationAngle -= 10;
        if (rotationAngle < -30) {
            rotationAngle = -30;
        }
    }

    private void pipeMove() {
        pipeSpawnLocation[0] -= 10;
        if (pipeSpawnLocation[0] < -300) {
            pipeSpawnLocation[0] = 690;
            Random random = new Random();
            int center = 200 + random.nextInt(800);
            pipeSpawnLocation[1] = center;

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    this.cancel();
                    point++;
                    pointSound.start();

                }
            }, 2700);
        }
    }
    private void setMove_background() {
        X_bg-=3;
        if (X_bg < -1000){
            X_bg = -200;
        }
    }

    public void run() {
        new Thread(() -> {
            while (!isDead) {
                if (holder.getSurface().isValid()) {
                    Canvas canvas = holder.lockCanvas();
                    if (canvas != null) {
                        if (isFly) {
                            fly();
                        } else {
                            drop();
                        }
                        setMove_background();
                        pipeMove();
                        drawOnCanvas(canvas);
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

    private void drawOnCanvas(Canvas canvas) {
        // Vẽ nền

        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(move_background, X_bg, 0, null);

        canvas.drawBitmap(pipe_bottom, pipeSpawnLocation[0], pipeSpawnLocation[1] + 200, null);
        canvas.drawBitmap(pipe_top, pipeSpawnLocation[0], pipeSpawnLocation[1] - 1600, null);

        Paint paint = new Paint();
        Matrix matrix = new Matrix();
        Paint paintText = new Paint();
        Paint bestScoreText = new Paint();
        paintText.setColor(Color.WHITE); // Màu chữ
        paintText.setTextSize(120); // Kích thước chữ
        paintText.setAntiAlias(true); // Làm mượt chữ
        bestScoreText.setColor(Color.WHITE); // Màu chữ
        bestScoreText.setTextSize(37); // Kích thước chữ
        bestScoreText.setAntiAlias(true); // Làm mượt chữ

        // Vẽ văn bản lên canvas
        String text = point + "";
        String bestText = "Điểm cao nhất: " + bestScore;
        float x = (float) (getWidth() / 2) - 50; // Vị trí X
        float y = 200; // Vị trí Y
        canvas.drawText(text, x, y, paintText);

        canvas.drawText(bestText, x-100, y + 70, bestScoreText);


        int centerX = bird.getWidth();
        int centerY = bird.getHeight();
        matrix.postRotate(rotationAngle, centerX, centerY);
        matrix.postTranslate(X, Y);

        canvas.rotate(10, 10, 10);

        if (!isHitPipeBottom() && isHitPipeTop()) {
            canvas.drawBitmap(bird, matrix, paint);
        } else {
            hitSound.start();
            isDead = true;
            flappyBirdStorage.UpdateBestScore(point);
            bestScore = flappyBirdStorage.GetBestScore();
            isFly = false;
            speed = 10;
            rotationAngle = 0;
            Y = 400;
            point = 0;
            pipeSpawnLocation = new int[]{680, 800};
            Intent mainPage = new Intent(flappyBirdControlActivityContext, FlappyBirdControlActivity.class);
            flappyBirdControlActivityContext.startActivity(mainPage);
        }

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
            onFly();
            MediaPlayer sound = MediaPlayer.create(getContext(), R.raw.flap);
            sound.start();
            return true;
        }
        return false;
    }

    public int[] getPipeSpawnLocation() {
        return pipeSpawnLocation;
    }

    public void setPipeSpawnLocation(int[] pipeSpawnLocation) {
        this.pipeSpawnLocation = pipeSpawnLocation;
    }

    private boolean isHitPipeBottom() {
        int currentX = X;
        int currentY = Y;
        int currentWidth = bird.getWidth();
        int currentHeight = bird.getHeight();

// Vị trí và kích thước của Bitmap khác (otherBitmap)
        int Xpipe_bottom = pipeSpawnLocation[0];
        int Ypipe_bottom = pipeSpawnLocation[1];


// Kiểm tra nếu vị trí (X, Y) có trùng với otherBitmap
        return !(currentX + currentWidth <= Xpipe_bottom + 200 ||  // Nếu bên phải của currentBitmap nằm bên trái của otherBitmap
                currentX >= Xpipe_bottom + pipe_bottom.getWidth() ||  // Nếu bên trái của currentBitmap nằm bên phải của otherBitmap
                currentY + currentHeight <= Ypipe_bottom + 200 || // Nếu phía dưới của currentBitmap nằm trên phía trên của otherBitmap
                currentY >= Ypipe_bottom + pipe_bottom.getHeight());  // Nếu phía trên của currentBitmap nằm dưới phía dưới của otherBitmap

    }

    private boolean isHitPipeTop() {
        int currentX = X;
        int currentY = Y;
        int currentWidth = bird.getWidth();
        int currentHeight = bird.getHeight();


        int Xpipe_top = pipeSpawnLocation[0];
        int Ypipe_top = pipeSpawnLocation[1] - 1730;


// Kiểm tra nếu vị trí (X, Y) có trùng với otherBitmap
        return (currentX + currentWidth <= Xpipe_top + 100 ||  // Nếu bên phải của currentBitmap nằm bên trái của otherBitmap
                currentX >= Xpipe_top + pipe_top.getWidth() ||  // Nếu bên trái của currentBitmap nằm bên phải của otherBitmap
                currentY + currentHeight <= Ypipe_top || // Nếu phía dưới của currentBitmap nằm trên phía trên của otherBitmap
                currentY >= Ypipe_top + pipe_top.getHeight());  // Nếu phía trên của currentBitmap nằm dưới phía dưới của otherBitmap

    }
}
