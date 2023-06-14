package com.example.testfinder.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfinder.viewholder.CommentsViewHolder;
import com.example.testfinder.R;
import com.example.testfinder.comment.Comment;
import com.example.testfinder.user.UserApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsViewHolder>{
    Context context;
    List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentsViewHolder(LayoutInflater.from(context).inflate(R.layout.comments_container, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        UserApiService.getInstance().findById(comments.get(holder.getBindingAdapterPosition()).getUser_id()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                holder.text_user_name.setText(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                holder.text_user_name.setText("Unknown");
                Log.e("DEB", "Error: "+ t);
            }
        });
        holder.text_comment.setText(comments.get(position).getText());
        Log.d("DEB", "Comments attached");
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}