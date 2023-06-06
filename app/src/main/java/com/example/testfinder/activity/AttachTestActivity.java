package com.example.testfinder.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.testfinder.R;
import com.example.testfinder.utility.Constants;
import com.example.testfinder.item.Item;
import com.example.testfinder.item.ItemsApiService;
import com.example.testfinder.utility.TextRecognizer;
import com.example.testfinder.databinding.ActivityAttachTestBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttachTestActivity extends AppCompatActivity {
    private TextRecognizer textRecognizer;
    private String DATA_PATH;
    boolean attached;
    private ActivityAttachTestBinding binding;
    private Bitmap chosen_bp;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences_school;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        binding = ActivityAttachTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        attached = false;
        //Path for saving training data for image recognition
        DATA_PATH = getApplicationContext().getFilesDir().getPath() + "/TesseractSample/";

        textRecognizer = new TextRecognizer();

        binding.addPhoto.setOnClickListener(v->{
            imageChooser();
        });
        binding.upload.setOnClickListener(v->{
            if(isValidAttach()) postData();
        });

        binding.btnBack.setOnClickListener(v->onBackPressed());

        binding.recognizetext.setOnClickListener(v->{
            textRecognizer.prepareTesseract(DATA_PATH, getApplicationContext());
            binding.progressRecognition.setVisibility(View.VISIBLE);
            binding.upload.setVisibility(View.INVISIBLE);
            startTextExtraction(chosen_bp);
        });

        String[] subjects = {"Maths", "History", "Russian", "Biology", "Physics", "Chemistry", "Informatics","Social Science", "English"};
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_subject_item, subjects);
        spinner_adapter.setDropDownViewResource(R.layout.spinner_dropdown_subject_item);
        binding.spinnerSubjects.setAdapter(spinner_adapter);
    }

    private void postData() {
        Bitmap bitmap = ((BitmapDrawable)binding.photo.getDrawable()).getBitmap();
        bitmap = checkBitmapSize(bitmap);
        String image = BitMapToString(bitmap);
        String inputName = binding.inputName.getText().toString();
        String inputDescription = binding.inputDescription.getText().toString();
        String recognized_text = binding.recognizedText.getText().toString();
        String subject = binding.spinnerSubjects.getSelectedItem().toString();
        int grade = Integer.parseInt(binding.grade.getText().toString());
        sharedPreferences = getSharedPreferences(Constants.USER_ID, MODE_PRIVATE);
        long user_id = sharedPreferences.getLong(Constants.USER_ID, -1);

        sharedPreferences_school = getSharedPreferences(Constants.USER_SCHOOL, MODE_PRIVATE);
        int user_school = sharedPreferences_school.getInt(Constants.USER_SCHOOL, 11);
        Item item = new Item(inputName, inputDescription,subject, image,null,null, grade, recognized_text, user_id, user_school);
        Call<Item> call = ItemsApiService.getInstance().postItem(item);
        binding.photo.setVisibility(View.GONE);
        binding.inputName.setVisibility(View.GONE);
        binding.inputDescription.setVisibility(View.GONE);
        binding.grade.setVisibility(View.GONE);
        binding.upload.setVisibility(View.GONE);
        binding.recognizedText.setVisibility(View.GONE);
        binding.recognizetext.setVisibility(View.GONE);
        binding.spinnerSubjects.setVisibility(View.GONE);
        binding.textSubject.setVisibility(View.GONE);
        binding.btnBack.setVisibility(View.GONE);
        binding.textAddtest.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(@NonNull Call<Item> call, @NonNull Response<Item> response) {
                Intent intent = new Intent(AttachTestActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onFailure(@NonNull Call<Item> call, @NonNull Throwable t) {
               showToast("Unable to upload photo");
                t.printStackTrace();
            }
        });
    }
    //decrease quality of image if more than 5mb
    private Bitmap checkBitmapSize(Bitmap bitmap) {
        while(bitmap.getByteCount() > 5000000){
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth()/1.2), (int) (bitmap.getHeight()/1.2), false);
        }
        return bitmap;
    }

    private void imageChooser()
    {
        Intent i = new Intent();
        //open image gallery or google files
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchActivity.launch(i);
    }
    //wait for user to chose the image
    ActivityResultLauncher<Intent> launchActivity = registerForActivityResult(new ActivityResultContracts
            .StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap = null;
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(),
                                    selectedImageUri);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        binding.addPhoto.setVisibility(View.GONE);
                        binding.photo.setVisibility(View.VISIBLE);
                        binding.recognizetext.setVisibility(View.VISIBLE);
                        binding.photo.setImageBitmap(selectedImageBitmap);
                        chosen_bp = selectedImageBitmap;
                        attached = true;
                    }
                }
            });
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    public boolean isValidAttach(){
        if(binding.inputName.getText().toString().trim().isEmpty()){
            showToast("Name can't be empty");
            return false;
        }
        else if(binding.inputDescription.getText().toString().trim().isEmpty()){
            showToast("Description can't be empty");
            return false;
        }
        else if(Integer.parseInt(binding.grade.getText().toString())<5 || Integer.parseInt(binding.grade.getText().toString())>11){
            showToast("Only 5-11 grades are supported");
            return false;
        }
        else if(!attached){
            showToast("Attach at least 1 photo");
            return false;
        }
        else return true;
    }
    private void showToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    //second thread for image recognition
    private class ExtractTextRunnable implements Runnable {
        private Bitmap bitmap;

        public ExtractTextRunnable(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public void run() {
            String extractedText = textRecognizer.extractText(bitmap, DATA_PATH);
            runOnUiThread(() -> updateUI(extractedText));
        }
    }

    private void startTextExtraction(Bitmap bitmap) {
        //a ThreadPoolExecutor configuration
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1, // corePoolSize
                1, // maximumPoolSize
                0L, // keepAliveTime
                TimeUnit.MILLISECONDS, // timeUnit
                new LinkedBlockingQueue<>() // workQueue
        );

        executor.execute(new ExtractTextRunnable(bitmap));
        executor.shutdown();
    }

    //update ui when text is recognized
    private void updateUI(String extractedText) {
        binding.recognizedText.setText(extractedText);
        binding.recognizedText.setVisibility(View.VISIBLE);
        binding.progressRecognition.setVisibility(View.INVISIBLE);
        binding.upload.setVisibility(View.VISIBLE);
    }
}