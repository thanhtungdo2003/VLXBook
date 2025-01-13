package com.vlteam.vlxbookapplication.httpservice;

import com.vlteam.vlxbookapplication.model.AuthModel;
import com.vlteam.vlxbookapplication.model.ChatMessagerSendReponse;
import com.vlteam.vlxbookapplication.model.MessageModel;
import com.vlteam.vlxbookapplication.model.UserInfoModel;
import com.vlteam.vlxbookapplication.model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("api/Account/login/{userName}&{password}")
    Call<AuthModel> login(@Path("userName") String username, @Path("password") String password);

    @POST("api/Account/sigin/{surname}&{name}&{userName}&{password}")
    Call<AuthModel> signin(@Path("surname") String surname, @Path("name") String name, @Path("userName") String userName, @Path("password") String password);

    @GET("api/Account/get-all-user/{page}")
    Call<List<UserInfoModel>> getAllUser(@Path("page") int page);
    @GET("api/Account/get-info-by-username/{userName}")
    Call<List<UserInfoModel>> getUser(@Path("userName") String userName);
    @GET("api/Messager/get-all-messager-by-username/{username}")
    Call<List<UserModel>> getAllMessagerBox(@Path("username") String username);

    @GET("api/Messager/get-all-chatmessager-by-id/{id}")
    Call<List<MessageModel>> getChatMessagerByID(@Path("id") String id);

    @POST("api/Messager/send/{username}&{messager_id}&{content}")
    Call<ChatMessagerSendReponse> sendMessager(@Path("username") String username, @Path("messager_id") String messager_id, @Path("content") String content);

    @POST("api/Messager/inbox-create/{username_self}&{username_target}")
    Call<UserModel> createBoxMess(@Path("username_self") String username_self, @Path("username_target") String username_target);
}

