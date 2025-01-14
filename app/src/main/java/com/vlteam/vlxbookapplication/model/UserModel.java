package com.vlteam.vlxbookapplication.model;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

public class UserModel {
    private String MessagerID;
    private String OtherUserNames;
    private String Content;
    private String AvataOther;
    private String FullName;
    private String FullNameOther;
    private Uri AvataUri;
    private String TimeOfSend;


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

