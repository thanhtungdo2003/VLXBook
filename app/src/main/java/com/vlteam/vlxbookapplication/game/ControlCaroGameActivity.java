package com.vlteam.vlxbookapplication.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vlteam.vlxbookapplication.R;

public class ControlCaroGameActivity extends AppCompatActivity {

    public static int width_board = 13;
    public static int win_amount_of_row = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_control_caro_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.join_caro_game), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            EditText winAmountOfRow = findViewById(R.id.editAmountWinRow_text);
            EditText w_txt = findViewById(R.id.editWidth_text);
            findViewById(R.id.caro_join_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    win_amount_of_row = Integer.parseInt(String.valueOf(winAmountOfRow.getText()));
                    width_board = Integer.parseInt(String.valueOf(w_txt.getText()));
                    Intent intent = new Intent(ControlCaroGameActivity.this, CaroGameActivity.class);
                    startActivity(intent);
                }
            });
            return insets;
        });
    }
}