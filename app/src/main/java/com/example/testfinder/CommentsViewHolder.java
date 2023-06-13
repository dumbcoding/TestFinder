package com.example.testfinder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentsViewHolder extends RecyclerView.ViewHolder {
    TextView text_user_name, text_comment;
    public CommentsViewHolder(@NonNull View itemView) {
        super(itemView);
        text_user_name = itemView.findViewById(R.id.text_user_name);
        text_comment = itemView.findViewById(R.id.text_comment);
    }
}