package com.example.cmpe243.googlemapstest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Asif N on 09/09/2014.
 */
public class DashboardActivity extends Activity{

    TextView textView, s1TextView, s2TextView, s3TextView;
    GestureDetector gestureDetector;

    ImageView car, background, speedGauge, speedNeedle;

    public static final int TEXT_SIZE = 60;

    private int count = 0;

    AnimationDrawable animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);
        //this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        textView = (TextView) findViewById(R.id.dashboardTextView);
        textView.setTextSize(60);

        s1TextView = (TextView) findViewById(R.id.s1TextView);
        //s2TextView = (TextView) findViewById(R.id.s2TestView);
        s3TextView = (TextView) findViewById(R.id.s3TextView);

        s1TextView.setTextSize(TEXT_SIZE);
//        s2TextView.setTextSize(TEXT_SIZE);
        s3TextView.setTextSize(TEXT_SIZE);

        car = (ImageView)findViewById(R.id.carSensor);
        background = (ImageView)findViewById(R.id.background_animation);
        speedGauge = (ImageView)findViewById(R.id.speedImageView);
        speedNeedle = (ImageView)findViewById(R.id.needleImageView);

        speedGauge.setMaxHeight(600);

        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams)car.getLayoutParams();
        params.width = 400;
        params.height = 400;

        Intent intent = getIntent();
        String message = intent.getStringExtra(ActivityMain.EXTRA_ACTIVITY);

        gestureDetector = new GestureDetector(new SwipeGestureDetector(this, MovingCarActivity.class,
                FreeRunActivity.class));
        background.setBackgroundResource(R.drawable.dashboard_background_normal);

        animation = (AnimationDrawable)background.getBackground();
        animation.start();

        s1TextView.setVisibility(View.INVISIBLE);
        s3TextView.setVisibility(View.INVISIBLE);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        count = IncomingDataChecker.count;
                        s1TextView.setText(""+count);
                        count +=10;
                        //s2TextView.setText(""+count);
                        count +=1;
                        s3TextView.setText(""+count);

                    }
                });

            }
        },0,100);

    }

    class MyAnimationRoutine extends TimerTask{
        MyAnimationRoutine(){

        }

        @Override
        public void run() {
            animation.start();

        }
    }

    class MyAnimationRoutine2 extends TimerTask{
        MyAnimationRoutine2(){

        }
        @Override
        public void run() {
            animation.stop();
        }
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
        getMenuInflater().inflate(R.menu.dashboard, menu);
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
