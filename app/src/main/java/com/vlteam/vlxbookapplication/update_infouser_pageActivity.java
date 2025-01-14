package com.vlteam.vlxbookapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vlteam.vlxbookapplication.httpservice.ApiService;
import com.vlteam.vlxbookapplication.httpservice.File;
import com.vlteam.vlxbookapplication.httpservice.FileManager;
import com.vlteam.vlxbookapplication.httpservice.FileStorageType;
import com.vlteam.vlxbookapplication.httpservice.RetrofitClient;
import com.vlteam.vlxbookapplication.model.ChatMessagerSendReponse;
import com.vlteam.vlxbookapplication.model.MessageModel;
import com.vlteam.vlxbookapplication.model.UserInfoModel;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class update_infouser_pageActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_MEDIA = 100; // Mã yêu cầu chọn ảnh/video
    private static final int PERMISSION_REQUEST_CODE = 1;  // Mã yêu cầu quyền
    ImageButton btnBack;
    private Uri currentAvataUri;
    private LinearLayout avataContainer;
    ImageView avataImage;
    Spinner spinday, spinmonth, spinyear;
    List<String> listDay, listMonth, listYear;
    ArrayAdapter<String> adapDay, adapMonth, adapYear;
    private FileManager fileManager;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.update_infouser_page);
        khoitaoSpinner();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fileManager = new File(this);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getUser(NewfeedActivity.username).enqueue(new Callback<List<UserInfoModel>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NonNull Call<List<UserInfoModel>> call, @NonNull Response<List<UserInfoModel>> response) {
                if (response.isSuccessful()) {
                    UserInfoModel user = response.body().get(0);

                    ((EditText) findViewById(R.id.surname_input)).setText(user.Surname);
                    ((EditText) findViewById(R.id.name_input)).setText(user.Name);
                    ((EditText) findViewById(R.id.location_input)).setText(user.Location);
                    ((EditText) findViewById(R.id.phone_input)).setText(user.Phone);
                    ((EditText) findViewById(R.id.job_input)).setText(user.Job);
                    LocalDateTime birth = LocalDateTime.parse(user.BirthOfDay);
                    spinyear.setSelection(listYear.indexOf(birth.getYear()+""));
                    spinmonth.setSelection(listMonth.indexOf(birth.getMonth()+""));
                    spinday.setSelection(listDay.indexOf(birth.getDayOfMonth()+""));

                    Call<ResponseBody> avataCall = apiService.downloadAvatar(user.Avata);
                    avataCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                boolean isSaved = fileManager.saveFileToInternalStorage(update_infouser_pageActivity.this, response.body(), user.Avata);
                                if (isSaved) {
                                    Uri fileUri = fileManager.getFileUri(update_infouser_pageActivity.this, user.Avata);
                                    avataImage.setImageURI(fileUri);

                                } else {
                                    // Xử lý lỗi khi lưu tệp
                                }
                            } else {
                                // Xử lý phản hồi không thành công
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            // Xử lý lỗi kết nối hoặc ngoại lệ
                        }
                    });
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<UserInfoModel>> call, @NonNull Throwable t) {
                Log.e("API_FAILURE", Objects.requireNonNull(t.getMessage()));
            }
        });

        findViewById(R.id.updateinfo_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        avataImage = findViewById(R.id.avataImage);
        avataContainer = findViewById(R.id.avataContainer);

        findViewById(R.id.addAvataButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    if (ContextCompat.checkSelfPermission(update_infouser_pageActivity.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(update_infouser_pageActivity.this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(update_infouser_pageActivity.this, Manifest.permission.READ_MEDIA_IMAGES) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(update_infouser_pageActivity.this, Manifest.permission.READ_MEDIA_VIDEO)) {
                            showPermissionRationale();
                        } else {
                            ActivityCompat.requestPermissions(update_infouser_pageActivity.this,
                                    new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO},
                                    PERMISSION_REQUEST_CODE);
                        }
                    } else {
                        openImagePicker();
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(update_infouser_pageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(update_infouser_pageActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    } else {
                        openImagePicker();
                    }
                }
            }
        });

        findViewById(R.id.save_info_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = NewfeedActivity.username;
                String surname = ((EditText) findViewById(R.id.surname_input)).getText().toString().trim();
                String name = ((EditText) findViewById(R.id.name_input)).getText().toString().trim();
                String phone = ((EditText) findViewById(R.id.phone_input)).getText().toString().trim();
                String job = ((EditText) findViewById(R.id.job_input)).getText().toString().trim();
                String location = ((EditText) findViewById(R.id.location_input)).getText().toString().trim();
                String birth = spinyear.getSelectedItem().toString() + "-" + spinmonth.getSelectedItem().toString() + "-" + spinday.getSelectedItem().toString();
                String avata = fileManager.getFileName(currentAvataUri);
                String coverphoto = "NONE";
                apiService.updateUserInfo(username,
                        name, surname, location, job, phone, coverphoto, avata, birth).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            Log.d("API_SUCCESS", response.toString());
                            Toast.makeText(update_infouser_pageActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("API_ERROR", "Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                        Log.e("API_FAILURE", Objects.requireNonNull(t.getMessage()));
                    }
                });
                fileManager.upload(currentAvataUri, FileStorageType.USER_AVATA);
            }
        });
    }

    public void khoitaoSpinner() {
        spinday = findViewById(R.id.spinDay);
        spinmonth = findViewById(R.id.spinMonth);
        spinyear = findViewById(R.id.spinYear);
        listDay = new ArrayList<>();
        listMonth = new ArrayList<>();
        listYear = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            @SuppressLint("DefaultLocale") String formattedNumber = String.format("%02d", i);
            listDay.add(formattedNumber);
        }
        for (int i = 1; i <= 12; i++) {
            @SuppressLint("DefaultLocale") String formattedNumber = String.format("%02d", i);
            listMonth.add(formattedNumber);
        }
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        for (int i = (currentYear - 150); i <= currentYear; i++) {
            listYear.add(i + "");
        }

        adapDay = new ArrayAdapter<String>(this, R.layout.spinner_items, listDay);
        adapMonth = new ArrayAdapter<String>(this, R.layout.spinner_items, listMonth);
        adapYear = new ArrayAdapter<String>(this, R.layout.spinner_items, listYear);
        adapDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinday.setAdapter(adapDay);
        spinmonth.setAdapter(adapMonth);
        spinyear.setAdapter(adapYear);
        int defaulyear = listYear.size() / 2;
        spinyear.setSelection(defaulyear);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/* video/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_MEDIA);
    }

    private void showPermissionRationale() {
        new AlertDialog.Builder(this)
                .setTitle("Cần quyền truy cập media")
                .setMessage("Ứng dụng cần quyền truy cập media để chọn ảnh hoặc video để đăng.")
                .setPositiveButton("Cấp quyền", (dialog, which) ->
                        ActivityCompat.requestPermissions(update_infouser_pageActivity.this,
                                new String[]{android.Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO},
                                PERMISSION_REQUEST_CODE)
                )
                .setNegativeButton("Hủy", null)
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_MEDIA && resultCode == RESULT_OK && data != null) {
            Uri selectedMediaUri = data.getData();
            ContentResolver resolver = getContentResolver();
            String type = resolver.getType(selectedMediaUri);

            if (type != null && type.startsWith("image/")) {
                if (isImageNearlySquare(selectedMediaUri)) {
                    addImageContainer(selectedMediaUri);
                } else {
                    Toast.makeText(update_infouser_pageActivity.this, "Ảnh phải có hình gần vuông!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public boolean isImageNearlySquare(Uri imageUri) {
        ContentResolver contentResolver = getContentResolver();
        String[] projection = {MediaStore.Images.Media.WIDTH, MediaStore.Images.Media.HEIGHT};
        Cursor cursor = contentResolver.query(imageUri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int widthIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH);
            int heightIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT);
            int width = cursor.getInt(widthIndex);
            int height = cursor.getInt(heightIndex);
            cursor.close();

            if (width > 0 && height > 0) {
                float aspectRatio = (float) width / height;
                return aspectRatio >= 0.9 && aspectRatio <= 1.1;
            }
        }

        return false;
    }

    private void addImageContainer(Uri uri) {
        if (uri != null) {
            avataImage.setImageURI(uri);
            currentAvataUri = uri;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Quyền truy cập bộ nhớ bị từ chối!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}