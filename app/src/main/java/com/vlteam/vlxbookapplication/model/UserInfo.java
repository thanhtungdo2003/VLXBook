package com.vlteam.vlxbookapplication.model;

import android.net.Uri;

public class UserInfo {
    public String UserName;
    public String UserInfoID;
    public String Surname;
    public Uri Avata;
    public Uri CoverPhoto;
    public String Name;
    public String BirthOfDay;
    public String Phone;
    public String Location;
    public String Job;

    public String getFullName() {
        return Name + " " + Surname;
    }
}
