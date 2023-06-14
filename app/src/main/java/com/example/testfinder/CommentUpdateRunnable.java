package com.example.testfinder;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfinder.adapter.CommentsAdapter;
import com.example.testfinder.comment.Comment;
import com.example.testfinder.comment.CommentApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentUpdateRunnable implements Runnable {
    private boolean isRunning = true;
    long item_id;
    RecyclerView CommentsRecycler;
    Context context;
    public CommentUpdateRunnable(long item_id, RecyclerView CommentsRecycler, Context context){
        this.item_id = item_id;
        this.CommentsRecycler = CommentsRecycler;
        this.context = context;
    }
    @Override
    public void run() {
        while (isRunning) {
            try {
                // Calling method to request comments from the server
                requestCommentsFromServer(item_id, CommentsRecycler, context);

                // Sleep for 10 seconds
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        isRunning = false;
    }

    private void requestCommentsFromServer(long item_id, RecyclerView CommentsRecycler, Context context) {
        CommentApiService.getInstance().findByItem(item_id).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(@NonNull Call<List<Comment>> call, @NonNull Response<List<Comment>> response) {
                CommentsRecycler.setAdapter(new CommentsAdapter(context, response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<List<Comment>> call, @NonNull Throwable t) {
                Log.e("DEB", "Unable to load comments: "+t);
            }
        });
    }
}
