package com.vlteam.vlxbookapplication.model;

import android.content.ContentResolver;
import android.content.Context;
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
            imageView.setImageURI(getAvataUri());
            return;
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
                            imageView.setImageURI(fileUri);
                        }
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
    public void renderAvata(ImageButton imageView){
        if (getAvataUri() != null){
            imageView.setImageURI(getAvataUri());
            return;
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
                            imageView.setImageURI(fileUri);
                        }
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

