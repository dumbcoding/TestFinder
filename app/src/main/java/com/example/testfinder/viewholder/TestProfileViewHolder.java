package com.example.testfinder.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfinder.R;


public class TestProfileViewHolder extends RecyclerView.ViewHolder {
    public TextView testProfileDescription;
    public TextView testProfileName;
    public TextView testProfileGrade;
    public ImageView imageView;
    public ImageButton delete_test;
    public TestProfileViewHolder(@NonNull View itemView) {
        super(itemView);
        testProfileName = itemView.findViewById(R.id.testProfileName);
        testProfileGrade = itemView.findViewById(R.id.testProfileGrade);
        testProfileDescription = itemView.findViewById(R.id.testProfileDescription);
        imageView = itemView.findViewById(R.id.image_view);
        delete_test = itemView.findViewById(R.id.delete_test);
    }
}
