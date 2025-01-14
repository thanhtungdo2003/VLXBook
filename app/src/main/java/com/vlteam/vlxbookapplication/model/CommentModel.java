package com.vlteam.vlxbookapplication.model;

public class CommentModel {
    private int avatar;
    private String userId;
    private String comment;
    private String userName;
    private int timeAgo;

    public CommentModel(int avatar, String userId, String comment, String userName, int timeAgo) {
        this.avatar = avatar;
        this.userId = userId;
        this.comment = comment;
        this.userName = userName;
        this.timeAgo = timeAgo;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public int getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(int timeAgo) {
        this.timeAgo = timeAgo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
