package com.vlteam.vlxbookapplication.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserInfoModel {
    public String UserName;
    public String UserInfoID;
    public String Surname;
    public String Avata;
    public String CoverPhoto;
    public String Name;
    public String BirthOfDay;
    public String Phone;
    public String Location;
    public String Job;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate getBirthOfDay(){
        return LocalDate.parse(BirthOfDay);
    }
}
