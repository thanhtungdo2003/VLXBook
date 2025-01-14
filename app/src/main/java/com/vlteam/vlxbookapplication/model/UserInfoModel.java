package com.vlteam.vlxbookapplication.model;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.vlteam.vlxbookapplication.InfoPage;
import com.vlteam.vlxbookapplication.R;
import com.vlteam.vlxbookapplication.httpservice.ApiService;
import com.vlteam.vlxbookapplication.httpservice.FileManager;

import java.time.LocalDate;
import java.time.LocalDateTime;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoModel {
    public String UserName;
    public String UserInfoID;
    public String Surname;
    public String Avata;
    public String CoverPhoto;
    public Uri AvataUri;
    public Uri CoverPhotoUri;
    public String Name;
    public String BirthOfDay;
    public String Phone;
    public String Location;
    public String Job;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate getBirthOfDay() {
        return LocalDate.parse(BirthOfDay);
    }

    public String getFullName() {
        return Surname + " " + Name;
    }

    public String getJob() {
        if (Location.equals("NONE")){
            return "Không có công việc";
        }
        return Job;
    }

    public String getLocation() {
        if (Location.equals("NONE")){
            return "Không có địa chỉ";
        }
        return Location;
    }

    public Uri getAvataUri() {
        return AvataUri;
    }

    public void setAvataUri(Uri avataUri) {
        AvataUri = avataUri;
    }

    public void renderAvata(ImageView imageView){
        if (getAvataUri() != null){
            imageView.setImageURI(getAvataUri());
            return;
        }
        if (Avata.equals("NONE")) {
            imageView.setImageResource(R.drawable.default_avatar);
        } else {
            Call<ResponseBody> avataCall = apiService.downloadAvatar(Avata);
            avataCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        boolean isSaved = fileManager.saveFileToInternalStorage(context, response.body(), Avata);
                        if (isSaved) {
                            Uri fileUri = fileManager.getFileUri(context, Avata);
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
        if (Avata.equals("NONE")) {
            imageView.setImageResource(R.drawable.default_avatar);
        } else {
            Call<ResponseBody> avataCall = apiService.downloadAvatar(Avata);
            avataCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        boolean isSaved = fileManager.saveFileToInternalStorage(context, response.body(), Avata);
                        if (isSaved) {
                            Uri fileUri = fileManager.getFileUri(context, Avata);
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

    @SuppressLint("SetTextI18n")
    public TextView getTextView(Context context, String attr){
        TextView detail_item = new TextView(context);
        detail_item.setTextSize(16);
        detail_item.setPadding(0, 13, 0, 0);
        switch (attr){
            case "location":
                if (Location.equals("NONE")){
                    detail_item.setText("Không có nơi sống để hiển thị");
                }else {
                    detail_item.setText("Sống tại: " + Location);
                }
                return detail_item;
            case "phone":
                if (Phone.equals("NONE")){
                    detail_item.setText("Không có số điện thoại");
                }else {
                    detail_item.setText("Số điện thoại: " + Phone);
                }
                return detail_item;
            case "birth":
                if (BirthOfDay.equals("NONE")){
                    detail_item.setText("Không có ngày sinh");
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        detail_item.setText("Ngày sinh: " + LocalDateTime.parse(BirthOfDay).toLocalDate().toString());
                    }
                }
                return detail_item;
            case "job":
                if (Job.equals("NONE")){
                    detail_item.setText("Không có công việc để hiển thị");
                }else {
                    detail_item.setText("Công việc: " + Job);
                }
                return detail_item;
        }
        return null;
    }
}
