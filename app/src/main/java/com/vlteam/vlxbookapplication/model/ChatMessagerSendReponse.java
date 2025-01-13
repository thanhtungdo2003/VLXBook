package com.vlteam.vlxbookapplication.model;

public class ChatMessagerSendReponse {
    public String ChatMessagerID;
    public String content;
    public String MessagerID;

    public String getChatMessagerID() {
        return ChatMessagerID;
    }

    public void setMessagerID(String messagerID) {
        MessagerID = messagerID;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setChatMessagerID(String chatMessagerID) {
        ChatMessagerID = chatMessagerID;
    }

    public String getContent() {
        return content;
    }

    public String getMessagerID() {
        return MessagerID;
    }
}
