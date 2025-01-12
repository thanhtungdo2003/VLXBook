package com.vlteam.vlxbookapplication.httpservice;

import com.vlteam.vlxbookapplication.model.AccountModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api/Account/get-by-username/{username}")
    Call<AccountModel> getAccount(@Path("username") String username);

    @POST("api/example")
    Call<String> postExample(@Body RequestBody body);
}

