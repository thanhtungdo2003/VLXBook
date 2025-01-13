package com.vlteam.vlxbookapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vlteam.vlxbookapplication.httpservice.ApiService;
import com.vlteam.vlxbookapplication.httpservice.RetrofitClient;
import com.vlteam.vlxbookapplication.model.AuthModel;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login extends AppCompatActivity {
    Button nextToSignup;
    private boolean isAsync = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login);
        nextToSignup = findViewById(R.id.btnNextToSignup);
        nextToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(login.this, SignupActivity.class);
                startActivity(signup);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.btnlogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAsync) return;
                String username = ((EditText) findViewById(R.id.usernameInput)).getText().toString();
                String pass = ((EditText) findViewById(R.id.passInput)).getText().toString();
                ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

                apiService.login(username, pass).enqueue(new Callback<AuthModel>() {
                    @Override
                    public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                        if (response.isSuccessful()) {
                            AuthModel token = response.body();
                            assert response.body() != null;
                            Log.d("API_SUCCESS", response.toString());

                            NewfeedActivity.userStorage.update(token.userName, pass);
                            Intent intent = new Intent(login.this, NewfeedActivity.class);
                            intent.putExtra("username", token.userName);
                            intent.putExtra("password", pass);
                            startActivity(intent);
                        } else {
                            Log.d("API_ERROR", "Code: " + response.code());
                            if (response.code() == 409){
                                Toast.makeText(login.this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                            }
                            isAsync = false;
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<AuthModel> call, @NonNull Throwable t) {
                        Log.e("API_FAILURE", Objects.requireNonNull(t.getMessage()));
                        isAsync = false;
                    }
                });
            }
        });
    }

}