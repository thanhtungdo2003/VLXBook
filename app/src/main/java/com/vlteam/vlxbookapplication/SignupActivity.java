package com.vlteam.vlxbookapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

public class SignupActivity extends AppCompatActivity {
    TextView nextToLogin;
    private static boolean isAsync = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signup);
        nextToLogin = findViewById(R.id.tvNextToLogin);
        nextToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(SignupActivity.this, login.class);
                startActivity(login);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnsignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String surname = ((EditText) findViewById(R.id.surname_input)).getText().toString().trim();
                String name = ((EditText) findViewById(R.id.name_input)).getText().toString().trim();
                String username = ((EditText) findViewById(R.id.usernameInputSignup)).getText().toString().trim();
                String pass = ((EditText) findViewById(R.id.passInputSignup)).getText().toString().trim();
                String re_pass = ((EditText) findViewById(R.id.passInputSignup2)).getText().toString().trim();
                if (surname.isEmpty() || name.isEmpty() || username.isEmpty() || pass.isEmpty() || re_pass.isEmpty()){
                    Toast.makeText(SignupActivity.this, "Thiếu thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pass.equals(re_pass)){
                    Toast.makeText(SignupActivity.this, "Mật khẩu nhập lại không chính xác!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

                apiService.signin(surname, name, username, pass).enqueue(new Callback<AuthModel>() {
                    @Override
                    public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                        if (response.isSuccessful()) {
                            AuthModel token = response.body();
                            assert response.body() != null;
                            Log.d("API_SUCCESS", response.toString());
                            NewfeedActivity.tokenAccount = token.token;
                            NewfeedActivity.username = token.userName;
                            System.out.println("=============================="+token.userName);
                            startActivity(new Intent(SignupActivity.this, NewfeedActivity.class));
                        } else {
                            Log.d("API_ERROR", "Code: " + response.code());
                            if (response.code() == 409){
                                Toast.makeText(SignupActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
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