package com.vlteam.vlxbookapplication.storage;

import java.util.ArrayList;
import java.util.List;

public class UserStorageModel {

    private final static String tableName = "user";
    private final static String username_col = "username";
    private final static String password_col = "password";

    public String getPassword_col() {
        return password_col;
    }

    public String getUsername_col() {
        return username_col;
    }

    public String getTableName() {
        return tableName;
    }


    public List<String> getDataStruct() {
        List<String> datas = new ArrayList<>();
        datas.add(username_col + " TEXT(50)");
        datas.add(password_col + " TEXT(255)");
        return datas;
    }
}
