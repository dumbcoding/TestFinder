package com.example.testfinder.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.example.testfinder.R;

import java.util.ArrayList;
import java.util.List;

public class FiltersFragment extends Fragment {

    private CheckBox grade5, grade6, grade7, grade8, grade9, grade10, grade11;
    private Spinner spinner_subjects;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filters, container, false);

        grade5 = view.findViewById(R.id.grade5);
        grade6 = view.findViewById(R.id.grade6);
        grade7 = view.findViewById(R.id.grade7);
        grade8 = view.findViewById(R.id.grade8);
        grade9 = view.findViewById(R.id.grade9);
        grade10 = view.findViewById(R.id.grade10);
        grade11 = view.findViewById(R.id.grade11);
        spinner_subjects = view.findViewById(R.id.spinner_subjects);

        String[] subjects = {"Maths", "History", "Russian", "Biology", "Physics", "Chemistry", "Informatics","Social Science", "English"};
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_subject_item, subjects);
        spinner_adapter.setDropDownViewResource(R.layout.spinner_dropdown_subject_item);
        spinner_subjects.setAdapter(spinner_adapter);

        return view;
    }
    public String getSelectedSpinnerItem() {
        return spinner_subjects.getSelectedItem().toString();
    }

    public List<Integer> getSelectedGrades(){
        List<Integer> res = new ArrayList<>();
        if(grade5.isChecked()) res.add(5);
        if(grade6.isChecked()) res.add(6);
        if(grade7.isChecked()) res.add(7);
        if(grade8.isChecked()) res.add(8);
        if(grade9.isChecked()) res.add(9);
        if(grade10.isChecked()) res.add(10);
        if(grade11.isChecked()) res.add(11);
        return res;
    }
}