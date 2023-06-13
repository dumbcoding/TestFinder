package com.example.testfinder.user;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {
    @GET("/user")
    Call<String> findById(@Query("id") long id);
    @GET("/user")
    Call<User> findByEmail(@Query("email") String email);
    @POST("/user")
    Call<User> postUser(@Body User user);
}
