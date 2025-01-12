package com.vlteam.vlxbookapplication.model;

public class UserModel {
    private String uid;
    private String name;
    private String message;
    private int imageResId;
    private String time; // Thêm thuộc tính giờ (dạng chuỗi)

    // Constructor
    public UserModel(String uid, String name, String message, int imageResId, String time) {
        this.uid = uid;
        this.name = name;
        this.message = message;
        this.imageResId = imageResId;
        this.time = time;  // Khởi tạo giờ
    }

    // Getters and setters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

