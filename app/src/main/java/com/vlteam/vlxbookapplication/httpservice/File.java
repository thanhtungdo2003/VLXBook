package com.vlteam.vlxbookapplication.httpservice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class File implements FileManager{
    private Context context;
    public File(Context context){
        this.context = context;
    }
    @Override
    public java.io.File uriToFile(Uri uri) {
        java.io.File file = null;
        try {
            // Lấy tên tệp từ Uri
            String fileName = getFileName(uri);
            // Tạo tệp tạm thời trong bộ nhớ cache của ứng dụng
            file = new java.io.File(context.getCacheDir(), fileName);
            file.createNewFile();

            // Mở InputStream từ Uri
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                // Sao chép dữ liệu từ InputStream vào tệp
                OutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                inputStream.close();
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
    @SuppressLint("Range")
    @Override
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    public MultipartBody.Part prepareFilePart(String partName, java.io.File file) {
        RequestBody requestFile = RequestBody.create(
                file,
                MediaType.parse("image/*")
        );
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
    public void uploadAvatar(java.io.File file) {
        // Chuẩn bị tệp
        MultipartBody.Part filePart = prepareFilePart("file", file);

        // Tạo instance của ApiService
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // Gọi phương thức uploadAvatar
        Call<Boolean> call = apiService.uploadAvatar(filePart);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean result = response.body();

                } else {
                    // Xử lý lỗi từ phản hồi
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // Xử lý lỗi kết nối hoặc ngoại lệ
            }
        });
    }
    public void downloadAvatar(Context context, String filename) {

    }
    @Override
    public boolean saveFileToInternalStorage(Context context, ResponseBody body, String filename) {
        try {
            java.io.File file = new java.io.File(context.getFilesDir(), filename);
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] buffer = new byte[4096];
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(buffer);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(buffer, 0, read);
                    fileSizeDownloaded += read;
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }
        } catch (IOException e) {
            return false;
        }
    }
    @Override
    public Uri getFileUri(Context context, String filename) {
        java.io.File file = new java.io.File(context.getFilesDir(), filename);
        return Uri.fromFile(file);
    }
    @Override
    public void upload(Uri uri, FileStorageType type) {
        switch (type){
            case USER_AVATA:
                uploadAvatar(uriToFile(uri));
                break;
            case ARTICLE_IMG:
                break;
            case USER_COVER_PHOTO:
                break;
        }
    }
    @Override
    public Uri download(Context context, String filename, FileStorageType type) {
        switch (type){
            case USER_AVATA:

                break;
            case ARTICLE_IMG:
                break;
            case USER_COVER_PHOTO:
                break;
        }
        return null;
    }
}
