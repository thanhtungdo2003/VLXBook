package com.vlteam.vlxbookapplication.model;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.util.Date;

public class MessageModel {
    public String ChatMessagerID ;
    public String chatMessagerID ;
    public String UserName ;
    public String MessagerID ;
    public String Content ;
    public String FullName ;
    public String Images ;
    public String TimeOfSend ;
    public Uri AvatarUri;
    public boolean isSent(String currentUserId) {
        return MessagerID.equals(currentUserId);
    }

    public MessageModel() {
    }

    public void setAvatarUri(Uri avatarUri) {
        AvatarUri = avatarUri;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDateTime getTimeOfSend() {
        return LocalDateTime.parse(TimeOfSend);
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public void setTimeOfSend(String timeOfSend) {
        TimeOfSend = timeOfSend;
    }

    public String getImages() {
        return Images;
    }

    public void setImages(String images) {
        Images = images;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getMessagerID() {
        return MessagerID;
    }

    public void setMessagerID(String messagerID) {
        MessagerID = messagerID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getChatMessagerID() {
        return ChatMessagerID;
    }

    public void setChatMessagerID(String chatMessagerID) {
        ChatMessagerID = chatMessagerID;
    }
}

