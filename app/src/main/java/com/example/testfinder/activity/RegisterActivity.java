package com.example.testfinder.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.testfinder.utility.Constants;
import com.example.testfinder.user.User;
import com.example.testfinder.user.UserApiService;
import com.example.testfinder.databinding.ActivityRegisterBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonSignUp.setOnClickListener(v->{
            if(isValidSignUp()) register();
        });
        binding.LogIn.setOnClickListener(v->onBackPressed());
    }
    private void register(){
        String name = binding.inputName.getText().toString();
        String email = binding.inputEmail.getText().toString();
        String password = binding.inputPassword.getText().toString();
        User user = new User((long) -1, name, password, email);
        Call<User> call = UserApiService.getInstance().postUser(user);
        loading(true);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                addUserId(response.body().getId());
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                showToast("Unable to Register. Try again later");
                Log.d("DEB", "error: "+t);
                loading(false);
            }
        });
    }

    private void addUserId(long id) {
        sharedPreferences = getSharedPreferences(Constants.USER_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences.edit();
        editor1.putLong(Constants.USER_ID, id);
        editor1.apply();
    }

    private Boolean isValidSignUp(){
        if(binding.inputName.getText().toString().trim().isEmpty()){
            showToast("Field name can't be empty");
            return false;
        }
        else if(binding.inputEmail.getText().toString().trim().isEmpty()){
            showToast("Field email can't be empty");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()){
            showToast("Invalid email");
            return false;
        }
        else if(binding.inputPassword.getText().toString().trim().isEmpty()){
            showToast("Field password can't be empty");
            return false;
        }
        else if(binding.confirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Confirm your password please");
            return false;
        }
        else if(!binding.inputPassword.getText().toString().equals(binding.confirmPassword.getText().toString())){
            showToast("Passwords should match");
            return false;
        }
        else return true;
    }
    private void showToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    private void loading(Boolean isLoading){
        if(isLoading) binding.buttonSignUp.setVisibility(View.INVISIBLE);
        else binding.buttonSignUp.setVisibility(View.VISIBLE);
    }
}