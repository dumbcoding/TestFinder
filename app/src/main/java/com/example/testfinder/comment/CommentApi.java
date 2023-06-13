package com.example.testfinder.comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CommentApi {
    @GET("/comment")
    Call<List<Comment>> findByItem(@Query("item_id") long item_id);

    @POST("/comment")
    Call<Comment> postComment(@Body Comment comment);
}
