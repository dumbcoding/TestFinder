package com.example.testfinder;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitService {
    private static final String baseUrl = "http://192.168.100.3:8080"; //local ip of your server
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null)
            return new Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create().asLenient()).baseUrl(baseUrl).build();
        return retrofit;
    }
}
