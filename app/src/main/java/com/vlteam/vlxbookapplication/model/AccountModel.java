package com.vlteam.vlxbookapplication.model;

public class AccountModel {
    public String userName;
    public String password;
    public String dateCreated;
    public String key;

    public String getKey() {
        return key;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
