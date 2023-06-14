package com.example.testfinder.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfinder.R;

public class CommentsViewHolder extends RecyclerView.ViewHolder {
    public TextView text_user_name;
    public TextView text_comment;
    public CommentsViewHolder(@NonNull View itemView) {
        super(itemView);
        text_user_name = itemView.findViewById(R.id.text_user_name);
        text_comment = itemView.findViewById(R.id.text_comment);
    }
}