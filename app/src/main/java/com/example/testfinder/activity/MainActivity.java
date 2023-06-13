package com.example.testfinder.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.testfinder.R;
import com.example.testfinder.utility.Constants;
import com.example.testfinder.item.Item;
import com.example.testfinder.item.ItemsApiService;
import com.example.testfinder.utility.RequestPermission;
import com.example.testfinder.TestAdapter;
import com.example.testfinder.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    SharedPreferences sharedPreferences_school;
    SharedPreferences sharedPreferences_id;
    private boolean isFragmentVisible = false;
    private FiltersFragment filtersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences_school = getSharedPreferences(Constants.USER_SCHOOL, MODE_PRIVATE);
        int user_school = sharedPreferences_school.getInt(Constants.USER_SCHOOL, 11);

        sharedPreferences_id = getSharedPreferences(Constants.USER_ID, MODE_PRIVATE);
        long user_id = sharedPreferences_id.getLong(Constants.USER_ID, -1);

        filtersFragment = new FiltersFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.FiltersFragmentContainer, filtersFragment).commit();
        binding.FiltersFragmentContainer.setVisibility(View.GONE);

        //open and close fragment with filters
        binding.drop.setOnClickListener(v->{
            if (isFragmentVisible) {
                binding.FiltersFragmentContainer.setVisibility(View.GONE);
                binding.drop.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_dropdown));
                isFragmentVisible = false;
            } else {
                binding.FiltersFragmentContainer.setVisibility(View.VISIBLE);
                binding.drop.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_dropup));
                isFragmentVisible = true;
            }
        });

        binding.profile.setOnClickListener(v->{
            ProfileFragment profileFragment = new ProfileFragment(user_id, user_id);
            profileFragment.show(getSupportFragmentManager(), "profile_dialog");
        });


        //ask for reading and writing storage permissions
        requestPermissions();

        binding.resultRecyclerView.setFocusable(false);
        binding.resultRecyclerView.setFocusableInTouchMode(false);
        List<Item> items = new ArrayList<>();

        //getting items by user school
        ItemsApiService.getInstance().getItemsBySchool(user_school).enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(@NonNull Call<List<Item>> call, @NonNull Response<List<Item>> response) {
                assert response.body() != null;
                for(Item i: response.body()){
                    items.add(new Item(i.getId(), i.getName(), i.getDescription(),i.getSubject(), i.getImage1(),null, null, i.getGrade(), i.getRecognized_text(), i.getUser_id(), i.getSchool()));
                }

                binding.resultRecyclerView.setAdapter(new TestAdapter(getApplicationContext(), items,
                        getSupportFragmentManager(),getCommentsLayout(),
                        getCommentsCloseView(), user_id, binding.settings, binding.attachPhoto));
                binding.resultRecyclerView.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<List<Item>> call,@NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"Unable to load tests", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
        binding.resultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.getresult.setOnClickListener(v->{
            binding.resultRecyclerView.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
            List<Item> selectedTests = getSelectedTests(items);
            binding.resultRecyclerView.setAdapter(new TestAdapter(getApplicationContext(), selectedTests,
                    getSupportFragmentManager(), getCommentsLayout(),
                    getCommentsCloseView(), user_id, binding.settings, binding.attachPhoto));
            binding.resultRecyclerView.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        });
        binding.attachPhoto.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, AttachTestActivity.class);
            startActivity(intent);
        });
        binding.settings.setOnClickListener(v-> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            recreate();
            //reload filter fragment
            getSupportFragmentManager().beginTransaction().remove(filtersFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.FiltersFragmentContainer, filtersFragment).commit();
            binding.swipeRefreshLayout.setRefreshing(false);
        });
    }

    //searching for tests which contain user input
    private List<Item> getSelectedTests(List<Item> items) {
        List<Integer> selectedGrades = filtersFragment.getSelectedGrades();
        String subject = filtersFragment.getSelectedSpinnerItem();
        List<Item> items_selected = new ArrayList<>();
        if(binding.inputSearch.getText().toString().isEmpty()) {
            for (Item i : items) {
                if (selectedGrades.contains(i.getGrade()) && subject.equals(i.getSubject())) items_selected.add(i);
            }
        }
        else{
            for (Item i : items) {
                if (selectedGrades.contains(i.getGrade()) && subject.equals(i.getSubject()) &&
                        (i.getName().toLowerCase().contains(binding.inputSearch.getText().toString().toLowerCase())||
                                i.getDescription().toLowerCase().contains(binding.inputSearch.getText().toString().toLowerCase())||
                                        i.getRecognized_text().toLowerCase().contains(binding.inputSearch.getText().toString().toLowerCase()))){
                    items_selected.add(i);
                }
            }
        }
        return items_selected;
    }


    private void requestPermissions() {
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        RequestPermission requestTool = new RequestPermission();
        requestTool.requestPermissions(this, permissions);
    }
    private FrameLayout getCommentsLayout(){
        return binding.CommentsLayout;
    }
    private View getCommentsCloseView(){
        return binding.commentsCloseView;
    }
}
