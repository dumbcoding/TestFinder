package com.example.testfinder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfinder.item.Item;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestViewHolder> {
    Context context;
    List<Item> items;

    public TestAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
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
