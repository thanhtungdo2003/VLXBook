package com.vlteam.vlxbookapplication.model;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.vlteam.vlxbookapplication.MessengerInterface;
import com.vlteam.vlxbookapplication.R;
import com.vlteam.vlxbookapplication.httpservice.ApiService;
import com.vlteam.vlxbookapplication.httpservice.FileManager;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserModel {
    private String MessagerID;
    private String OtherUserNames;
    private String Content;
    private String AvataOther;
    private String FullName;
    private String FullNameOther;
    private Uri AvataUri;
    private String TimeOfSend;

    private Context context;
    private ApiService apiService;
    private FileManager fileManager;


    public ApiService getApiService() {
        return apiService;
    }

    public void setApiService(ApiService apiService) {
        this.apiService = apiService;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void renderAvata(ImageView imageView){
        if (getAvataUri() != null){
            Bitmap optimizedBitmap = optimizeBitmapTo1MB(getAvataUri());
            if (optimizedBitmap != null) {
                imageView.setImageBitmap(optimizedBitmap);
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }            return;
        }
        if (getAvataOther().equals("NONE")) {
            imageView.setImageResource(R.drawable.default_avatar);

        } else {
            Call<ResponseBody> avataCall = apiService.downloadAvatar(getAvataOther());
            avataCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        boolean isSaved = fileManager.saveFileToInternalStorage(context, response.body(), getAvataOther());
                        if (isSaved) {
                            Uri fileUri = fileManager.getFileUri(context, getAvataOther());
                            setAvataUri(fileUri);
                            Bitmap optimizedBitmap = optimizeBitmapTo1MB(fileUri);
                            if (optimizedBitmap != null) {
                                imageView.setImageBitmap(optimizedBitmap);
                                imageView.setVisibility(View.VISIBLE);
                            } else {
                                imageView.setVisibility(View.GONE);
                            }                        }
                    }else {
                        imageView.setImageResource(R.drawable.default_avatar);

                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    imageView.setImageResource(R.drawable.default_avatar);
                }
            });
        }
    }
    public void setUpAvataUri(){
        if (!getAvataOther().equals("NONE")) {
            Call<ResponseBody> avataCall = apiService.downloadAvatar(getAvataOther());
            avataCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        boolean isSaved = fileManager.saveFileToInternalStorage(context, response.body(), getAvataOther());
                        if (isSaved) {
                            Uri fileUri = fileManager.getFileUri(context, getAvataOther());
                            setAvataUri(fileUri);
                        }
                    }else {
                        setAvataUri(null);
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    setAvataUri(null);
                }
            });
        }
    }
    public void renderAvata(ImageButton imageView){
        if (getAvataUri() != null){
            Bitmap optimizedBitmap = optimizeBitmapTo1MB(getAvataUri());
            if (optimizedBitmap != null) {
                imageView.setImageBitmap(optimizedBitmap);
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }            return;
        }
        if (getAvataOther().equals("NONE")) {
            imageView.setImageResource(R.drawable.default_avatar);

        } else {
            Call<ResponseBody> avataCall = apiService.downloadAvatar(getAvataOther());
            avataCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        boolean isSaved = fileManager.saveFileToInternalStorage(context, response.body(), getAvataOther());
                        if (isSaved) {
                            Uri fileUri = fileManager.getFileUri(context, getAvataOther());
                            setAvataUri(fileUri);
                            Bitmap optimizedBitmap = optimizeBitmapTo1MB(fileUri);
                            if (optimizedBitmap != null) {
                                imageView.setImageBitmap(optimizedBitmap);
                                imageView.setVisibility(View.VISIBLE);
                            } else {
                                imageView.setVisibility(View.GONE);
                            }                        }
                    }else {
                        imageView.setImageResource(R.drawable.default_avatar);

                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    imageView.setImageResource(R.drawable.default_avatar);

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
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 15, byteStream);

            // Kiểm tra kích thước sau nén
            while (byteStream.toByteArray().length / 1024 > 1024) {
                byteStream.reset();
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 15, byteStream);
            }

            // Trả về bitmap đã tối ưu hóa
            return BitmapFactory.decodeByteArray(byteStream.toByteArray(), 0, byteStream.size());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private int calculateInSampleSizeToTargetSize(int originalWidth, int originalHeight) {
        int reqWidth = 100; // Đặt chiều rộng mong muốn
        int reqHeight = 100; // Đặt chiều cao mong muốn
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
    // Getters and setters
    public String getMessagerID() {
        return MessagerID;
    }

    public String getAvataOther() {
        return AvataOther;
    }

    public void setAvataOther(String avataOther) {
        AvataOther = avataOther;
    }

    public void setMessagerID(String messagerID) {
        this.MessagerID = messagerID;
    }

    public String getOtherUserNames() {
        return OtherUserNames;
    }

    public String getFullNameOther() {
        return FullNameOther;
    }

    public void setFullNameOther(String fullNameOther) {
        FullNameOther = fullNameOther;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public void setOtherUserNames(String otherUserNames) {
        OtherUserNames = otherUserNames;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        this.Content = content;
    }

    public Uri getAvataUri() {
        return AvataUri;
    }

    public void setAvataUri(Uri avataUri) {
        AvataUri = avataUri;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDateTime getTimeOfSend() {
        return LocalDateTime.parse(TimeOfSend);
    }

    public void setTimeOfSend(String timeOfSend) {
        this.TimeOfSend = timeOfSend;
    }
}

