package com.vlteam.vlxbookapplication.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.vlteam.vlxbookapplication.InfoPage;

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
    public LocalDate getBirthOfDay() {
        return LocalDate.parse(BirthOfDay);
    }

    public String getFullName() {
        return Surname + " " + Name;
    }

    public String getJob() {
        if (Location.equals("NONE")){
            return "Không có công việc";
        }
        return Job;
    }

    public String getLocation() {
        if (Location.equals("NONE")){
            return "Không có địa chỉ";
        }
        return Location;
    }

    @SuppressLint("SetTextI18n")
    public TextView getTextView(Context context, String attr){
        TextView detail_item = new TextView(context);
        detail_item.setTextSize(16);
        detail_item.setPadding(0, 13, 0, 0);
        switch (attr){
            case "location":
                if (Location.equals("NONE")){
                    detail_item.setText("Không có nơi sống để hiển thị");
                }else {
                    detail_item.setText("Sống tại: " + Location);
                }
                return detail_item;
            case "phone":
                if (Phone.equals("NONE")){
                    detail_item.setText("Không có số điện thoại");
                }else {
                    detail_item.setText("Số điện thoại: " + Phone);
                }
                return detail_item;
            case "birth":
                if (BirthOfDay.equals("NONE")){
                    detail_item.setText("Không có ngày sinh");
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        detail_item.setText("Ngày sinh: " + LocalDateTime.parse(BirthOfDay).toLocalDate().toString());
                    }
                }
                return detail_item;
            case "job":
                if (Job.equals("NONE")){
                    detail_item.setText("Không có công việc để hiển thị");
                }else {
                    detail_item.setText("Công việc: " + Job);
                }
                return detail_item;
        }
        return null;
    }
}
