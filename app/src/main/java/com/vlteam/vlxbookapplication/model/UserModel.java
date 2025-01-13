package com.vlteam.vlxbookapplication.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

public class UserModel {
    private String MessagerID;
    private String OtherUserNames;
    private String Content;
    private String FullName;
    private String FullNameOther;
    private int imageResId;
    private String TimeOfSend; // Thêm thuộc tính giờ (dạng chuỗi)


    // Getters and setters
    public String getMessagerID() {
        return MessagerID;
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

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDateTime getTimeOfSend() {
        return LocalDateTime.parse(TimeOfSend);
    }

    public void setTimeOfSend(String timeOfSend) {
        this.TimeOfSend = timeOfSend;
    }
}

