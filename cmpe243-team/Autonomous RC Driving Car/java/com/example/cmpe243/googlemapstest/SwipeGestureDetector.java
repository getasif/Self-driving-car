package com.example.cmpe243.googlemapstest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Asif N on 09/09/2014.
 */

public class SwipeGestureDetector
        extends GestureDetector.SimpleOnGestureListener {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 200;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    Context contextSource;
    Class<?> contextDestinationLeft, contextDestinationRight;

    SwipeGestureDetector(Context source, Class<?> destinationLeft, Class<?> destinationRight){
        this.contextSource = source;
        this.contextDestinationLeft = destinationLeft;
        this.contextDestinationRight = destinationRight;
    }

    private void onLeftSwipe() {
        Log.d("Activity", "Left");
        Intent intent = new Intent(contextSource, contextDestinationLeft);
        contextSource.startActivity(intent);
    }

    private void onRightSwipe() {
        Log.d("Activity", "Right");
        Intent intent = new Intent(contextSource, contextDestinationRight);
        contextSource.startActivity(intent);
    }

    private void onDownSwipe() {
        Log.d("Activity", "Down");
    }

    private void onUpSwipe() {
        Log.d("Activity", "Up");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2,
                           float velocityX, float velocityY) {
        try {
            float diffAbs = Math.abs(e1.getY() - e2.getY());
            float diff = e1.getX() - e2.getX();

            float yDistance = Math.abs(e1.getY() - e2.getY());
            if (diffAbs > SWIPE_MAX_OFF_PATH)
                return false;

            // Left swipe
            if (diff > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                SwipeGestureDetector.this.onLeftSwipe();

                // Right swipe
            } else if (-diff > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                SwipeGestureDetector.this.onRightSwipe();
            } else if (Math.abs(velocityY) > this.SWIPE_THRESHOLD_VELOCITY && yDistance > this.SWIPE_MIN_DISTANCE){
                if(e1.getY() > e2.getY())
                    SwipeGestureDetector.this.onUpSwipe();
                else
                    SwipeGestureDetector.this.onDownSwipe();
            }

        } catch (Exception e) {
            Log.e("YourActivity", "Error on gestures");
        }
        return false;
    }
}
