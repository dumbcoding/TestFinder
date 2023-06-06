package com.example.testfinder.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.testfinder.R;
import com.example.testfinder.databinding.ActivityMainBinding;
import com.example.testfinder.databinding.ActivitySettingsBinding;
import com.example.testfinder.utility.Constants;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences_id;
    SharedPreferences sharedPreferences_school;
    private ActivitySettingsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnBack.setOnClickListener(v->onBackPressed());
        sharedPreferences_school = getSharedPreferences(Constants.USER_SCHOOL, MODE_PRIVATE);
        int user_school = sharedPreferences_school.getInt(Constants.USER_SCHOOL, 11);
        //setting up spinner with available schools
        String[] options = {"11", "12", "13"};
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(SettingsActivity.this, R.layout.spinner_item, options);
        spinner_adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        binding.spinner.setAdapter(spinner_adapter);
        binding.spinner.setOnItemSelectedListener(this);
        int spinner_pos = spinner_adapter.getPosition(Integer.toString(user_school));
        binding.spinner.setSelection(spinner_pos);

        binding.btnLogout.setOnClickListener(v->logOut());
        binding.textLogout.setOnClickListener(v->logOut());
    }

    private void logOut(){
        sharedPreferences = getSharedPreferences(Constants.IS_LOGGED_IN, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.IS_LOGGED_IN, false);
        editor.apply();

        sharedPreferences_id = getSharedPreferences(Constants.USER_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor_id = sharedPreferences_id.edit();
        editor_id.putLong(Constants.USER_ID, -1);
        editor_id.apply();
        Intent intent = new Intent(SettingsActivity.this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        sharedPreferences_school = getSharedPreferences(Constants.USER_SCHOOL, MODE_PRIVATE);
        SharedPreferences.Editor editor_school = sharedPreferences_school.edit();
        switch (position) {
            case 0:
                editor_school.putInt(Constants.USER_SCHOOL, 11);
                editor_school.apply();
                break;
            case 1:
                editor_school.putInt(Constants.USER_SCHOOL, 12);
                editor_school.apply();
                break;
            case 2:
                editor_school.putInt(Constants.USER_SCHOOL, 13);
                editor_school.apply();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        sharedPreferences_school = getSharedPreferences(Constants.USER_SCHOOL, MODE_PRIVATE);
        int user_school = sharedPreferences_school.getInt(Constants.USER_SCHOOL, 11);
        SharedPreferences.Editor editor_school = sharedPreferences_school.edit();
        editor_school.putInt(Constants.USER_SCHOOL, user_school);
        editor_school.apply();
    }
}