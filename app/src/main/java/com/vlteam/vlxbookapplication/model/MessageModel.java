package com.vlteam.vlxbookapplication.model;

import java.util.Date;

public class MessageModel {
    public String ChatMessagerID ;
    public String UserName ;
    public String MessagerID ;
    public String Content ;
    public String Images ;
    public Date TimeOfSend ;
    public boolean isSent(String currentUserId) {
        return MessagerID.equals(currentUserId);
    }

    public MessageModel(String content, String chatMessagerID, String userName, String messagerID, String images, Date timeOfSend) {
        Content = content;
        ChatMessagerID = chatMessagerID;
        UserName = userName;
        MessagerID = messagerID;
        Images = images;
        TimeOfSend = timeOfSend;
    }

    public MessageModel() {
    }

    public Date getTimeOfSend() {
        return TimeOfSend;
    }

    public void setTimeOfSend(Date timeOfSend) {
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

