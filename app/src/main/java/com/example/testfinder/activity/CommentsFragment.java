package com.example.testfinder.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testfinder.CommentUpdateRunnable;
import com.example.testfinder.adapter.CommentsAdapter;
import com.example.testfinder.R;
import com.example.testfinder.comment.Comment;
import com.example.testfinder.comment.CommentApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsFragment extends Fragment {
    List<Comment> commentsList;
    long item_id;
    long user_id;
    RecyclerView CommentsRecycler;
    EditText input_message;
    ImageView send_message;
    private CommentUpdateRunnable commentUpdateRunnable;
    private Thread commentUpdateThread;
    public CommentsFragment() {
        // Required empty public constructor
    }
    public CommentsFragment(List<Comment> commentsList, long item_id, long user_id) {
        this.commentsList = commentsList;
        this.item_id = item_id;
        this.user_id = user_id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        input_message = view.findViewById(R.id.inputMessage);
        send_message = view.findViewById(R.id.send_message);
        CommentsRecycler = view.findViewById(R.id.CommentsRecycler);
        CommentsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        CommentsRecycler.setAdapter(new CommentsAdapter(getContext(), commentsList));

        send_message.setOnClickListener(v->{
            String text = input_message.getText().toString();
            Comment comment = new Comment(null, user_id, null, item_id, text);
            CommentApiService.getInstance().postComment(comment).enqueue(new Callback<Comment>() {
                @Override
                public void onResponse(@NonNull Call<Comment> call, @NonNull Response<Comment> response) {
                    Toast.makeText(getContext(), "Loading...", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Call<Comment> call, @NonNull Throwable t) {
                    Log.e("DEB", "Unable to post comment: "+ t);
                }
            });
        });
        commentUpdateRunnable = new CommentUpdateRunnable(item_id, CommentsRecycler, getContext());
        commentUpdateThread = new Thread(commentUpdateRunnable);
        commentUpdateThread.start();

        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        // Stop the comment update thread when the fragment is destroyed
        commentUpdateRunnable.stop();
        commentUpdateThread.interrupt();
    }

}