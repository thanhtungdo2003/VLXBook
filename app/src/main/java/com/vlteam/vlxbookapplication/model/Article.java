package com.vlteam.vlxbookapplication.model;

import java.util.Date;

public class Article {
    public String UserName;
    public String ArticleID;
    public String Caption;
    public String Images;
    public Date TimeOfPost;
    public String Videos;

    public Article(String userName, String articleID, String caption, String images, Date timeOfPost, String videos) {
        this.UserName = userName;
        this.ArticleID = articleID;
        this.Caption = caption;
        this.Images = images;
        this.TimeOfPost = timeOfPost;
        this.Videos = videos;
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

    public Date getTimeOfPost() {
        return TimeOfPost;
    }

    public void setTimeOfPost(Date timeOfPost) {
        this.TimeOfPost = timeOfPost;
    }

    public String getVideos() {
        return Videos;
    }

    public void setVideos(String videos) {
        this.Videos = videos;
    }
}
