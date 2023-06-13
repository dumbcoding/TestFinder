package com.example.testfinder.activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testfinder.R;
import com.example.testfinder.TestProfileAdapter;
import com.example.testfinder.item.Item;
import com.example.testfinder.item.ItemsApiService;
import com.example.testfinder.user.UserApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends DialogFragment {
    long user_id;
    long from_user_id;
    private TextView user_name;
    private RecyclerView testsRecyclerView;
    private ImageButton btn_back;
    public ProfileFragment() {}
    public ProfileFragment(long user_id, long from_user_id) {
        this.user_id = user_id;
        this.from_user_id = from_user_id;
    }

    @Override
    public int getTheme(){
        return R.style.FullScreenDialog;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_TestFinder);
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user_name = view.findViewById(R.id.user_name);
        testsRecyclerView = view.findViewById(R.id.testsRecyclerView);
        testsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btn_back = view.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v->{dismiss();});

        List<Item> items = new ArrayList<>();
        UserApiService.getInstance().findById(user_id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                user_name.setText(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e("DEB", "Error: "+ t);
                Log.e("DEB", "User ID "+ user_id);
            }
        });

        ItemsApiService.getInstance().getItemsByUserId(user_id).enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(@NonNull Call<List<Item>> call, @NonNull Response<List<Item>> response) {
                assert response.body() != null;
                for(Item i: response.body()){
                    items.add(new Item(i.getId(), i.getName(), i.getDescription(),i.getSubject(), i.getImage1(),null, null, i.getGrade(), i.getRecognized_text(), i.getUser_id(), i.getSchool()));
                }

                testsRecyclerView.setAdapter(new TestProfileAdapter(getContext(), items, from_user_id));
                testsRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<List<Item>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(),"Unable to load tests", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

}