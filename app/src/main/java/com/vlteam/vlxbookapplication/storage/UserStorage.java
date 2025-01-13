package com.vlteam.vlxbookapplication.storage;
import com.vlteam.vlxbookapplication.NewfeedActivity;
import com.vlteam.vlxbookapplication.SimpleDatabaseManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserStorage {
    private final SimpleDatabaseManager databaseManager;
    private static final String table_name = "user";
    private UserStorageModel model;
    public static NewfeedActivity context;
    public UserStorage(NewfeedActivity context){
        databaseManager = new SimpleDatabaseManager(context);
        model = new UserStorageModel();
        databaseManager.createSimpleTable(table_name, model.getDataStruct());
        UserStorage.context = context;
    }

    public void update(String username, String password){
        databaseManager.dropTable(table_name);
        databaseManager.createSimpleTable(table_name, model.getDataStruct());
        HashMap<String, Object> datas = new HashMap<>();
        datas.put(model.getUsername_col(), username);
        datas.put(model.getPassword_col(), password);
        databaseManager.insert(table_name, datas);
    }
    public String getUserName(){
        List<Object> usernames = databaseManager.getColumnValues(table_name, model.getUsername_col());
        if (usernames != null && !usernames.isEmpty()) {
            return usernames.get(0).toString();
        }
        return "";
    }
    public String getPassword(){
        List<Object> passwords = databaseManager.getColumnValues(table_name, model.getPassword_col());
        if (passwords != null && !passwords.isEmpty()) {
            return passwords.get(0).toString();
        }
        return "";
    }
}
