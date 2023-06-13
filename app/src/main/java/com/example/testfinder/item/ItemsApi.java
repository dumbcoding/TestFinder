package com.example.testfinder.item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ItemsApi {
    @GET("/item")
    Call<List<Item>> getItemsBySchool(@Query("school") int school);
    @GET("/item")
    Call<List<Item>> getItemsByUserId(@Query("from_user_id") long user_id);

    @POST("/item")
    Call<Item> postItem(@Body Item item);
    @DELETE("/item/{id}")
    Call<Void> deleteItem(@Path("id") long id);

}
