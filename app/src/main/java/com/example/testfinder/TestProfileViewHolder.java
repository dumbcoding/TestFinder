package com.example.testfinder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class TestProfileViewHolder extends RecyclerView.ViewHolder {
    TextView testProfileDescription, testProfileName, testProfileGrade;
    ImageView imageView;
    ImageButton delete_test;
    public TestProfileViewHolder(@NonNull View itemView) {
        super(itemView);
        testProfileName = itemView.findViewById(R.id.testProfileName);
        testProfileGrade = itemView.findViewById(R.id.testProfileGrade);
        testProfileDescription = itemView.findViewById(R.id.testProfileDescription);
        imageView = itemView.findViewById(R.id.image_view);
        delete_test = itemView.findViewById(R.id.delete_test);
    }
}
