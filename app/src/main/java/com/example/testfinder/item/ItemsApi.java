package com.example.testfinder.item;

import com.example.testfinder.item.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ItemsApi {
    @GET("/item")
    Call<List<Item>> getItemsBySchool(@Query("school") int school);

    @POST("/item")
    Call<Item> postItem(@Body Item item);

}
