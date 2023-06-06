package com.example.testfinder.user;

import com.example.testfinder.RetrofitService;
import com.example.testfinder.user.UserApi;

public class UserApiService {
    private static UserApi userApi;

    public static UserApi getInstance(){
        if(userApi == null) userApi = RetrofitService.getInstance().create(UserApi.class);
        return userApi;
    }
}
