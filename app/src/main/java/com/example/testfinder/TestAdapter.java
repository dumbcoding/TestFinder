package com.example.testfinder;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfinder.activity.CommentsFragment;
import com.example.testfinder.activity.ProfileFragment;
import com.example.testfinder.comment.Comment;
import com.example.testfinder.comment.CommentApiService;
import com.example.testfinder.item.Item;
import com.example.testfinder.user.UserApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestAdapter extends RecyclerView.Adapter<TestViewHolder> {
    Context context;
    List<Item> items;
    FragmentManager fragmentManager;
    FrameLayout commentsLayout;
    FloatingActionButton settings, newTest;
    long from_user_id;
    View commentsCloseView;

    public TestAdapter(Context context, List<Item> items, FragmentManager fragmentManager,
                       FrameLayout commentsLayout,View commentsCloseView,
                       long from_user_id, FloatingActionButton settings, FloatingActionButton newTest) {
        this.context = context;
        this.items = items;
        this.fragmentManager = fragmentManager;
        this.commentsLayout = commentsLayout;
        this.from_user_id = from_user_id;
        this.commentsCloseView = commentsCloseView;
        this.settings = settings;
        this.newTest = newTest;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TestViewHolder(LayoutInflater.from(context).inflate(R.layout.tests_container, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        holder.testName.setText(items.get(position).getName());
        holder.testDescription.setText(items.get(position).getDescription());
        holder.testGrade.setText("Grade: "+ items.get(position).getGrade());
        Bitmap bitmap = StringToBitMap(items.get(position).getImage1());
        setImageWebView(holder, bitmap);

        UserApiService.getInstance().findById(items.get(holder.getBindingAdapterPosition()).getUser_id()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                holder.textUserName.setText(response.body());
                holder.textUserName.setOnClickListener(v->{
                    ProfileFragment profileFragment = new ProfileFragment(items.get(holder.getBindingAdapterPosition()).getUser_id(), from_user_id);
                    profileFragment.show(fragmentManager, "profile_dialog");
                });
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                holder.textUserName.setText("Unknown");
                Log.e("DEB", "Error :"+ t);
            }
        });
        //loading comments for every item
        CommentApiService.getInstance().findByItem(items.get(holder.getBindingAdapterPosition()).getId()).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(@NonNull Call<List<Comment>> call, @NonNull Response<List<Comment>> response) {
                holder.comments_btn.setOnClickListener(v->{
                    CommentsFragment commentsFragment = new CommentsFragment(response.body(), items.get(holder.getBindingAdapterPosition()).getId(), from_user_id);
                    commentsLayout.setVisibility(View.VISIBLE);
                    commentsCloseView.setVisibility(View.VISIBLE);
                    settings.setVisibility(View.GONE);
                    newTest.setVisibility(View.GONE);
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).replace(R.id.CommentsLayout, commentsFragment).commit();
                    commentsCloseView.setOnClickListener(b->{
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_down, R.anim.slide_down).remove(commentsFragment).commit();
                        commentsCloseView.setVisibility(View.GONE);
                        commentsLayout.setVisibility(View.GONE);
                        settings.setVisibility(View.VISIBLE);
                        newTest.setVisibility(View.VISIBLE);
                    });
                });
            }

            @Override
            public void onFailure(@NonNull Call<List<Comment>> call, @NonNull Throwable t) {
                Log.d("DEB", "Unable to load comments: "+t);
            }
        });
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
    private String getHtmlData(String bodyHTML) {
        //make image appear in 100% size
        String head = "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }
    public void setImageWebView(TestViewHolder holder, Bitmap bitmap){
        String html="<html><body><img src='{IMAGE_PLACEHOLDER}' /></body></html>";

        // Convert bitmap to Base64 encoded image for web
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        String image = "data:image/png;base64," + imageBase64;

        // load image to webview
        html = html.replace("{IMAGE_PLACEHOLDER}", image);
        holder.webView.setBackgroundColor(0xFFFFFF);
        holder.webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        holder.webView.getSettings().setBuiltInZoomControls(true);
        holder.webView.getSettings().setDisplayZoomControls(false);

        holder.webView.loadDataWithBaseURL("file:///android_asset/", getHtmlData(html), "text/html", "utf-8", "");
    }
}
