package com.vlteam.vlxbookapplication.game;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vlteam.vlxbookapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CaroGameActivity extends AppCompatActivity {
    //Bàn cờ 9x14 ô
    private List<View> checkeds = new ArrayList<>();

    private int count_of_col = 0;
    public int amount_check_win = 5;
    private HashMap<Integer, View> slots = new HashMap<>();
    private boolean confirm = false;
    private View confirmView;
    private int X, Y = 0;
    private boolean checked = false;
    private TextView current_checked_label;
    private int currentStep = 1;
    private int slotSize = 50;

    private HashMap<Integer, int[][]> playStepMatrix = new HashMap<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_caro_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.join_caro_game), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            slotSize = (int) (650 / ControlCaroGameActivity.width_board);
            current_checked_label = findViewById(R.id.current_checked_text_view);
            current_checked_label.setText("〇");
            current_checked_label.setTextColor(Color.rgb(250, 100, 100));
            ConstraintLayout main = findViewById(R.id.join_caro_game);
            findViewById(R.id.caro_reload).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder al = new AlertDialog.Builder(CaroGameActivity.this);
                    al.setTitle("Reload lại trò chơi");
                    al.setMessage("Bạn có xác nhận khởi động lại trò chơi?");
                    al.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            reload();
                        }
                    });
                    al.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    al.create().show();
                }
            });
            findViewById(R.id.caro_undo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentStep < 1) return;
                    currentStep--;
                    loadStep(currentStep);
                }
            });
            findViewById(R.id.caro_redo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentStep > playStepMatrix.size() - 2) return;
                    currentStep++;
                    loadStep(currentStep);
                }
            });
            for (int i = 0; i < (ControlCaroGameActivity.width_board * ControlCaroGameActivity.width_board); i++) {
                TextView button = new TextView(this);
                slots.put(i, button);
                button.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, // Chiều rộng
                        LinearLayout.LayoutParams.WRAP_CONTENT  // Chiều cao
                ));
                button.setWidth(slotSize);
                button.setHeight(slotSize);
                button.setText("_");
                button.setX(X);
                button.setY(Y);
                button.setTextSize((float) (slotSize/2.55));
                button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                button.setPadding(5, 0, 5, 3);
                button.setBackgroundColor(Color.WHITE);
                main.addView(button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((TextView) v).getText() == "✘" || ((TextView) v).getText() == "〇")
                            return; //Dừng hoạt động nếu ô đã đánh
                        if (!confirm) {
                            v.setBackgroundColor(Color.rgb(255, 250, 160));
                            confirmView = v;
                            confirm = true;
                            return;
                        } else {
                            if (!confirmView.equals(v)) {
                                confirmView.setBackgroundColor(Color.WHITE);
                                v.setBackgroundColor(Color.rgb(255, 250, 160));
                                confirmView = v;
                                confirm = true;
                                return;
                            }
                        }
                        confirm = false;
                        checkeds.add(v);
                        if (checked) {
                            checked = false;
                            v.setBackgroundColor(Color.WHITE);
                            ((TextView) v).setTextColor(Color.rgb(100, 130, 250));
                            ((TextView) v).setText("✘");
                            current_checked_label.setText("〇");
                            current_checked_label.setTextColor(Color.rgb(250, 100, 100));
                        } else {
                            checked = true;
                            ((TextView) v).setTextColor(Color.rgb(250, 100, 100));
                            ((TextView) v).setText("〇");
                            v.setBackgroundColor(Color.WHITE);
                            current_checked_label.setText("✘");
                            current_checked_label.setTextColor(Color.rgb(100, 130, 250));
                        }
                        boolean end = false;
                        int winner = 0;

                        if (checkWinner(getBoard()) == 1) {
                            end = true;
                            winner = 1;
                            Toast.makeText(CaroGameActivity.this, "✘ thắng", Toast.LENGTH_LONG).show();
                        } else if (checkWinner(getBoard()) == 2) {
                            end = true;
                            winner = 2;
                            Toast.makeText(CaroGameActivity.this, "〇 thắng", Toast.LENGTH_LONG).show();
                        }else {
                            onBOT();
                        }
                        if (playStepMatrix.isEmpty()) {
                            playStepMatrix.put(0, getBoard());
                        } else {
                            playStepMatrix.put(playStepMatrix.size(), getBoard());
                        }
                        if (currentStep >= (playStepMatrix.size() - 1))
                            currentStep = playStepMatrix.size();
                        if (end) {
                            AlertDialog.Builder alWin = new AlertDialog.Builder(CaroGameActivity.this);
                            if (winner == 1) {
                                alWin.setTitle("✘ Thắng");
                            } else {
                                alWin.setTitle("〇 Thắng");
                            }
                            alWin.setMessage("Chơi tiếp?");
                            alWin.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    reload();
                                }
                            });
                            alWin.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(CaroGameActivity.this, ControlCaroGameActivity.class));
                                }
                            });
                            alWin.create().show();
                        }

                    }
                });
                if (count_of_col >= ControlCaroGameActivity.width_board-1) {
                    count_of_col = 0;
                    Y += slotSize + 5;
                    X = 0;
                } else {
                    X += slotSize + 5;
                    count_of_col++;
                }
            }
            return insets;
        });
    }

    private void reload() {
        X = 0;
        Y = 0;
        checkeds.clear();
        for (int slot : slots.keySet()) {
            TextView v = (TextView) slots.get(slot);
            if (v != null) {
                v.setBackgroundColor(Color.WHITE);
                v.setText("");
            }
        }
    }

    private int[][] getBoard() {
        int col = 0;
        int row = 0;
        int[][] board = new int[ControlCaroGameActivity.width_board][ControlCaroGameActivity.width_board];
        for (int slot : slots.keySet()) {
            if (col > ControlCaroGameActivity.width_board - 1) {
                row++;
                col = 0;
            }
            int check = 0;
            TextView v = (TextView) slots.get(slot);
            assert v != null;
            if (v.getText() == "✘") {
                check = 1;
            } else if (v.getText() == "〇") {
                check = 2;
            }
            board[col][row] = check;
            col++;
        }
        return board;
    }

    private int checkWinner(int[][] board) {
        int N = board.length;
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == 0) continue;
                for (int[] dir : directions) {
                    int dx = dir[0], dy = dir[1];
                    int count = 1;
                    for (int step = 1; step < ControlCaroGameActivity.win_amount_of_row; step++) {
                        int x = i + step * dx;
                        int y = j + step * dy;
                        if (x < 0 || x >= N || y < 0 || y >= N || board[x][y] != board[i][j]) break;
                        count++;
                    }
                    if (count == ControlCaroGameActivity.win_amount_of_row) {
                        return board[i][j];
                    }
                }
            }
        }
        return 0;
    }

    private void loadStep(int step) {
        if (!playStepMatrix.containsKey(step)) return;
        int[][] matrix = playStepMatrix.get(step);
        int slot = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                String icon = "";
                int colorText = 0;
                switch (matrix[i][j]) {
                    case 0:
                        colorText = Color.WHITE;
                        break;
                    case 1:
                        colorText = Color.rgb(100, 130, 250);
                        icon = "✘";
                        break;
                    case 2:
                        colorText = Color.rgb(250, 100, 100);
                        icon = "〇";
                        break;
                }
                ((TextView) slots.get(slot)).setText(icon);
                ((TextView) slots.get(slot)).setTextColor(colorText);
                slot++;
            }
        }
    }
    private void onBOT(){

    }
}