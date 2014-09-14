package com.example.cmpe243.googlemapstest;

import android.app.ActionBar;
import android.app.Activity;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;


/**
 * Created by Asif N on 09/09/2014.
 */

public class LedBlinkActivity extends Activity{

    ImageView redLed, greenLed;
    GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_blink);

        redLed = (ImageView)findViewById(R.id.redLedImageView);
        greenLed = (ImageView)findViewById(R.id.greenLedImageView);

        Animation animation = new AlphaAnimation(1,0);
        animation.setDuration(500);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);

        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) redLed.getLayoutParams();
        params.width = 50;
        params.height = 50;
        ViewGroup.LayoutParams params2 = (ViewGroup.LayoutParams) greenLed.getLayoutParams();
        params2.width = 50;
        params2.height = 50;
        redLed.setLayoutParams(params);
        greenLed.setLayoutParams(params2);

        redLed.startAnimation(animation);
        greenLed.startAnimation(animation);

        gestureDetector = new GestureDetector(new SwipeGestureDetector(this, FreeRunActivity.class,
                ActivityMain.class));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.led_blink, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
