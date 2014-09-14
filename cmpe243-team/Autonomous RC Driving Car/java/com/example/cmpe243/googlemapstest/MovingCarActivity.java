package com.example.cmpe243.googlemapstest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import junit.framework.TestCase;

/**
 * Created by Asif N on 09/09/2014.
 */

public class MovingCarActivity extends Activity {

    ImageView carSideView, carShadow;
    ImageButton powerButton;

    TextView raceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving_car);

        carSideView = (ImageView)findViewById(R.id.carSideImageView);
        carShadow = (ImageView)findViewById(R.id.carShadowImageView);

        raceText = (TextView)findViewById(R.id.raceTextView);
        powerButton = (ImageButton)findViewById(R.id.powerImageButton);

        final TranslateAnimation animation = new TranslateAnimation(800.0f, 100.0f, 0.0f, 0.0f);

        animation.setDuration(3000);
        animation.setRepeatCount(0);
        animation.setRepeatMode(Animation.RESTART);

        carSideView.setVisibility(View.INVISIBLE);

        powerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carSideView.startAnimation(animation);
                carShadow.startAnimation(animation);
                carSideView.setVisibility(View.INVISIBLE);
                carShadow.setVisibility(View.INVISIBLE);
                raceText.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.moving_car, menu);
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
