package com.example.testfinder.item;

import com.example.testfinder.RetrofitService;
import com.example.testfinder.item.ItemsApi;

public class ItemsApiService {

    private static ItemsApi itemsApi;

    public static ItemsApi getInstance(){
        if(itemsApi == null) itemsApi = RetrofitService.getInstance().create(ItemsApi.class);
        return itemsApi;
    }
}
