package com.example.testfinder.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.testfinder.utility.Constants;
import com.example.testfinder.user.User;
import com.example.testfinder.user.UserApiService;
import com.example.testfinder.databinding.ActivityLogInBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {
    private ActivityLogInBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //skip authorization if user is logged in
        sharedPreferences = getSharedPreferences(Constants.IS_LOGGED_IN, MODE_PRIVATE);
        if(sharedPreferences.getBoolean(Constants.IS_LOGGED_IN, false)){
            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonSignIn.setOnClickListener(v->{
            if(isValidSignIn()) logIn();
        });
        binding.createNewAccount.setOnClickListener(v->{
            Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
    private void logIn(){
        String email = binding.inputEmail.getText().toString();
        String password = binding.inputPassword.getText().toString();
        loading(true);
        //database query to find user by email and check if password is correct
        UserApiService.getInstance().findByEmail(email).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                boolean enter = false;
                boolean wrong_password = false;
                User user = new User(response.body().getId(), response.body().getName(), response.body().getPassword(), response.body().getEmail());
                if(user.getPassword().equals(password)){
                    addUserId(user.getId());
                    enter = true;
                }
                else{
                    wrong_password = true;
                    Toast.makeText(getApplicationContext(),"Wrong password", Toast.LENGTH_LONG).show();
                }
                if(enter){
                    saveData();
                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                if(!enter && !wrong_password) Toast.makeText(getApplicationContext(),"No such user", Toast.LENGTH_LONG).show();
                loading(false);
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"No such user", Toast.LENGTH_LONG).show();
                t.printStackTrace();
                loading(false);
            }
        });
    }

    //save user id to shared preferences
    private void addUserId(long id) {
        sharedPreferences_id = getSharedPreferences(Constants.USER_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences.edit();
        editor1.putLong(Constants.USER_ID, id);
        editor1.apply();
    }

    private void loading(Boolean isLoading){
        if(isLoading) binding.buttonSignIn.setVisibility(View.INVISIBLE);
        else binding.buttonSignIn.setVisibility(View.VISIBLE);
    }
    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.IS_LOGGED_IN, true);
        editor.apply();
    }
    private Boolean isValidSignIn() {
        if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Field email can't be empty");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Field password can't be empty");
            return false;
        }
        else return true;
    }
    private void showToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}