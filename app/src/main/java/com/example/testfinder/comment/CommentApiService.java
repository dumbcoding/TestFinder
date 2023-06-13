package com.example.testfinder.comment;

import com.example.testfinder.RetrofitService;

public class CommentApiService {
    private static CommentApi commentApi;
    public static CommentApi getInstance(){
        if(commentApi == null) commentApi = RetrofitService.getInstance().create(CommentApi.class);
        return commentApi;
    }
}
