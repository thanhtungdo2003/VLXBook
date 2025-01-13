package com.vlteam.vlxbookapplication.model;

public class ChatMessagerSendReponse {
    public String chatMessagerID;
    public String content;
    public String MessagerID;

    public String getChatMessagerID() {
        return chatMessagerID;
    }

    public void setMessagerID(String messagerID) {
        MessagerID = messagerID;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setChatMessagerID(String chatMessagerID) {
        chatMessagerID = chatMessagerID;
    }

    public String getContent() {
        return content;
    }

    public String getMessagerID() {
        return MessagerID;
    }
}
