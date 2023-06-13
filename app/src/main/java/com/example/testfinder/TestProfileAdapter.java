package com.example.testfinder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfinder.item.Item;
import com.example.testfinder.item.ItemsApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestProfileAdapter extends RecyclerView.Adapter<TestProfileViewHolder>{
        Context context;
        List<Item> items;
        long from_user_id;

        public TestProfileAdapter(Context context, List<Item> items, long from_user_id) {
            this.context = context;
            this.items = items;
            this.from_user_id = from_user_id;
        }

        @NonNull
        @Override
        public TestProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TestProfileViewHolder(LayoutInflater.from(context).inflate(R.layout.tests_profile_container, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull TestProfileViewHolder holder, int position) {
            holder.testProfileName.setText(items.get(position).getName());
            holder.testProfileDescription.setText(items.get(position).getDescription());
            holder.testProfileGrade.setText("Grade: "+ items.get(position).getGrade());
            Bitmap bitmap = StringToBitMap(items.get(position).getImage1());
            holder.imageView.setImageBitmap(bitmap);
            if(items.get(holder.getBindingAdapterPosition()).getUser_id() == from_user_id) {
                Log.d("DEB", "UserID " + from_user_id +" "+ items.get(holder.getBindingAdapterPosition()).getUser_id());
                holder.delete_test.setOnClickListener(v -> {
                    ItemsApiService.getInstance().deleteItem(items.get(holder.getBindingAdapterPosition()).getId()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                            holder.testProfileName.setVisibility(View.GONE);
                            holder.testProfileDescription.setVisibility(View.GONE);
                            holder.testProfileGrade.setVisibility(View.GONE);
                            holder.imageView.setVisibility(View.GONE);
                            holder.delete_test.setVisibility(View.GONE);
                            Toast.makeText(v.getContext(), "Successfully deleted", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                            Toast.makeText(v.getContext(), "Unable to delete", Toast.LENGTH_LONG).show();
                            t.printStackTrace();
                        }
                    });
                });
            }
            else{
                holder.delete_test.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
        public Bitmap StringToBitMap(String encodedString){
            try {
                byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
                return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            } catch(Exception e) {
                Log.d("DEB", "Error occurred" + e.getMessage());
                return null;
            }
        }
}
