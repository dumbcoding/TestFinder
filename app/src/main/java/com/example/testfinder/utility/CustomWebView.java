package com.example.testfinder.utility;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebView;
//custom web view for touch control between image enlarging and scrolling recycler view
public class CustomWebView extends WebView {

    private GestureDetector gestureDetector;

    public CustomWebView(Context context) {
        super(context);
        gestureDetector = new GestureDetector(context, new GestureListener());
    }
    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Pass the touch event to the gesture detector
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // Disable scrolling in RecyclerView when zooming is in progress
            if (getScale() > 1.0f) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }
}