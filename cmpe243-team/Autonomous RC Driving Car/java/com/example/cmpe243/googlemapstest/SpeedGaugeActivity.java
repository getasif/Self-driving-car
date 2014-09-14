package com.example.cmpe243.googlemapstest;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;


/**
 * Created by Asif N on 09/09/2014.
 */

public class SpeedGaugeActivity extends Activity {

    Button runButton;
    ImageView needle;

    private Matrix matrix;
    private int framePerSeconds = 100;
    private long animationDuration = 10000;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_gauge);

        runButton = (Button)findViewById(R.id.runButton);

        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                needle = (ImageView)findViewById(R.id.needle);
                RotateAnimation rotateAnimation = new RotateAnimation(0f, 90f, 105f, 33f);
                rotateAnimation.setInterpolator(new LinearInterpolator());

                rotateAnimation.setRepeatCount(Animation.INFINITE);
                rotateAnimation.setDuration(2500);

                needle.startAnimation(rotateAnimation);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.speed_gauge, menu);
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

