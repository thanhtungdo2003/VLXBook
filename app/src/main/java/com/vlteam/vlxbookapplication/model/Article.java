package com.vlteam.vlxbookapplication.model;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.vlteam.vlxbookapplication.R;
import com.vlteam.vlxbookapplication.httpservice.ApiService;
import com.vlteam.vlxbookapplication.httpservice.FileManager;

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

    public void renderImg(ImageView imageView, String path){
        if (getImgArray().length == 0){
            imageView.setVisibility(View.GONE);
            return;
        }
        if (imgUris.containsKey(path)){
            if (imgUris.get(path) != null) {
                imageView.setImageURI(imgUris.get(path));
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
                            imgUris.put(path, fileUri);
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
