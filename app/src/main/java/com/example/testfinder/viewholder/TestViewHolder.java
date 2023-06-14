package com.example.testfinder.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfinder.R;
import com.example.testfinder.utility.CustomWebView;

public class TestViewHolder extends RecyclerView.ViewHolder {
    public TextView testDescription;
    public TextView testName;
    public TextView testGrade;
    public TextView textUserName;
    public CustomWebView webView;
    public ImageView comments_btn;
    public TestViewHolder(@NonNull View itemView) {
        super(itemView);
        testName = itemView.findViewById(R.id.testName);
        testGrade = itemView.findViewById(R.id.testGrade);
        testDescription = itemView.findViewById(R.id.testDescription);
        textUserName = itemView.findViewById(R.id.text_user_name);
        webView = itemView.findViewById(R.id.web_view);
        comments_btn = itemView.findViewById(R.id.comments_btn);
    }
}
