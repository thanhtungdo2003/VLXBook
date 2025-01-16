package com.vlteam.vlxbookapplication.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.vlteam.vlxbookapplication.R;
import com.vlteam.vlxbookapplication.httpservice.ApiService;
import com.vlteam.vlxbookapplication.httpservice.FileManager;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Article {
    public String UserName;
    public String ArticleID;
    public String Caption;
    public String Images;
    public String TimeOfPost;
    public String Videos;
    private Context context;
    private ApiService apiService;
    private FileManager fileManager;
    private String FullName;
    public HashMap<String, Uri> imgUris = new HashMap<>();


    public void setUp(ApiService apiService, Context context, FileManager fileManager){
        this.apiService = apiService;
        this.context = context;
        this.fileManager = fileManager;

        for (String path: getImgArray()){
            imgUris.put(path, null);
        }
    }
    public String[] getImgArray(){
        if (Images.contains("&")){
            return Images.split("&");
        }else {
            return new String[]{Images};
        }
    }

    public void renderImg(ImageView imageView, String path) {
        imageView.setVisibility(View.GONE);

        if (getImgArray().length == 0) {
            return;
        }
        if (imgUris.containsKey(path)) {
            if (imgUris.get(path) != null) {
                Bitmap optimizedBitmap = optimizeBitmapTo1MB(imgUris.get(path));
                if (optimizedBitmap != null) {
                    imageView.setImageBitmap(optimizedBitmap);
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.GONE);
                }
                return;
            }
        }
        if (path.equals("NONE")) {
            imageView.setVisibility(View.GONE);
        } else {
            Call<ResponseBody> avataCall = apiService.downloadArticleImg(path);
            avataCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        boolean isSaved = fileManager.saveFileToInternalStorage(context, response.body(), path);
                        if (isSaved) {
                            Uri fileUri = fileManager.getFileUri(context, path);

                            // Giảm kích thước ảnh trước khi hiển thị
                            Bitmap optimizedBitmap = optimizeBitmapTo1MB(fileUri);
                            if (optimizedBitmap != null) {
                                imgUris.put(path, fileUri);

                                imageView.setImageBitmap(optimizedBitmap);
                                imageView.setVisibility(View.VISIBLE);
                            } else {
                                imageView.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        imageView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    imageView.setVisibility(View.GONE);
                }
            });
        }
    }

    private Bitmap optimizeBitmapTo1MB(Uri fileUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(fileUri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            // Đọc metadata của ảnh (không tải ảnh vào bộ nhớ)
            BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

            // Kích thước gốc của ảnh
            int originalWidth = options.outWidth;
            int originalHeight = options.outHeight;

            // Tính toán tỷ lệ giảm để phù hợp
            options.inSampleSize = calculateInSampleSizeToTargetSize(originalWidth, originalHeight);
            options.inJustDecodeBounds = false;

            // Đọc lại ảnh với tỷ lệ giảm
            inputStream = context.getContentResolver().openInputStream(fileUri);
            Bitmap scaledBitmap = BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

            // Nén ảnh xuống dung lượng ~1MB
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteStream);

            // Kiểm tra kích thước sau nén
            while (byteStream.toByteArray().length / 1024 > 1024) {
                byteStream.reset();
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 45, byteStream);
            }

            // Trả về bitmap đã tối ưu hóa
            return BitmapFactory.decodeByteArray(byteStream.toByteArray(), 0, byteStream.size());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private int calculateInSampleSizeToTargetSize(int originalWidth, int originalHeight) {
        int reqWidth = 600; // Đặt chiều rộng mong muốn
        int reqHeight = 600; // Đặt chiều cao mong muốn
        int inSampleSize = 1;

        if (originalHeight > reqHeight || originalWidth > reqWidth) {
            int halfHeight = originalHeight / 2;
            int halfWidth = originalWidth / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public String getArticleID() {
        return ArticleID;
    }

    public void setArticleID(String articleID) {
        this.ArticleID = articleID;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        this.Caption = caption;
    }

    public String getImages() {
        return Images;
    }

    public void setImages(String images) {
        this.Images = images;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDateTime getTimeOfPost() {
        return LocalDateTime.parse(TimeOfPost);
    }

    public void setTimeOfPost(String timeOfPost) {
        this.TimeOfPost = timeOfPost;
    }

    public String getVideos() {
        return Videos;
    }

    public void setVideos(String videos) {
        this.Videos = videos;
    }
}
