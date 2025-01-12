package com.vlteam.vlxbookapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class createStatusPage extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_MEDIA = 100; // Mã yêu cầu chọn ảnh/video
    private static final int PERMISSION_REQUEST_CODE = 1;  // Mã yêu cầu quyền
    ImageButton btnBack;
    LinearLayout addImageVideo;
    private HorizontalScrollView scrollView;
    private LinearLayout imageContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_status_page);

        btnBack =findViewById(R.id.btnBackToNewfeed);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(createStatusPage.this,NewfeedActivity.class);
                back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(back);
            }
        });
        // Ánh xạ các view từ layout XML
        addImageVideo = findViewById(R.id.addImageVideo);
        scrollView = findViewById(R.id.scrollImageHorizontal);
        imageContainer = findViewById(R.id.imageContainer);

        addImageVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    if (ContextCompat.checkSelfPermission(createStatusPage.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(createStatusPage.this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(createStatusPage.this, Manifest.permission.READ_MEDIA_IMAGES) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(createStatusPage.this, Manifest.permission.READ_MEDIA_VIDEO)) {
                            showPermissionRationale();
                        } else {
                            ActivityCompat.requestPermissions(createStatusPage.this,
                                    new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO},
                                    PERMISSION_REQUEST_CODE);
                        }
                    } else {
                        openImagePicker();
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(createStatusPage.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(createStatusPage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    } else {
                        openImagePicker();
                    }
                }
            }
        });
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
                        ActivityCompat.requestPermissions(createStatusPage.this,
                                new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO},
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

            if (type != null && type.startsWith("video/")) {
                addImageToScrollView(selectedMediaUri, true); // Video
            } else if (type != null && type.startsWith("image/")) {
                addImageToScrollView(selectedMediaUri, false); // Ảnh
            }
        }
    }

    private void addImageToScrollView(Uri uri,boolean isVideo) {
        if (uri != null) {
            FrameLayout frameLayout = new FrameLayout(this);
            frameLayout.setLayoutParams(new LinearLayout.LayoutParams(500, 500));
            frameLayout.setBackgroundResource(R.drawable.imgvideo_border);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();

            params.setMargins(0, 0, 10, 0);

            frameLayout.setLayoutParams(params);
            ImageButton btnDelete = new ImageButton(this);
            btnDelete.setLayoutParams(new FrameLayout.LayoutParams(100, 100));  // Đặt kích thước 100x100
            btnDelete.setScaleType(ImageView.ScaleType.CENTER_CROP);  // Thiết lập ScaleType
            btnDelete.setImageResource(R.drawable.trashicon);  // Thiết lập hình ảnh
            btnDelete.setBackgroundResource(android.R.color.transparent);  // Thiết lập nền trong suốt

            FrameLayout.LayoutParams deleteParams = (FrameLayout.LayoutParams) btnDelete.getLayoutParams();
            deleteParams.gravity= (Gravity.TOP | Gravity.RIGHT);
            btnDelete.setLayoutParams(deleteParams);  // Cập nhật LayoutParams với margin

            btnDelete.setOnClickListener(v -> {
                imageContainer.removeView(frameLayout);
            });
            if(isVideo){

                VideoView videoView = new VideoView(this);
                videoView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)); // Đặt kích thước cho video
                videoView.setVideoURI(uri);
                videoView.seekTo(1);

                videoView.setOnClickListener(v -> {
                    if (videoView.isPlaying()) {
                        videoView.pause();
                    } else {
                        videoView.start();
                    }
                });
                frameLayout.addView(videoView);
                frameLayout.addView(btnDelete);
                imageContainer.addView(frameLayout);

            }
            else {
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(500, 500));
                imageView.setImageURI(uri);
                frameLayout.addView(imageView);
                frameLayout.addView(btnDelete);
                imageContainer.addView(frameLayout);
            }
            scrollView.setVisibility(View.VISIBLE);
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
