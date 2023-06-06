package com.example.testfinder.user;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {
    @GET("/user")
    Call<List<User>> getUsers();

    @GET("/user/{email}")
    Call<User> findByEmail(@Path("email") String email);

    @POST("/user")
    Call<User> postUser(@Body User user);
}
