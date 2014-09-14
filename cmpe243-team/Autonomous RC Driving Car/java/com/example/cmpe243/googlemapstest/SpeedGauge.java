package com.example.cmpe243.googlemapstest;

import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Asif N on 09/09/2014.
 */

/* ---------------- XML Layout file -------------------------------------------
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:background="#000000"
    tools:context=".MyActivity">


    <ImageView

        android:id="@+id/meter"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentLeft="true"
        android:layout_alignParentTop="true"
        android:layout_marginLeft="223dp"
        android:layout_marginTop="26dp"
        android:src="@drawable/meter" />

    <ImageView

        android:id="@+id/needle"
        android:layout_width="90dp"
        android:layout_height="30dp"
        android:layout_marginLeft="330dp"
        android:layout_marginTop="137dp"
        android:src="@drawable/meter_needle"
        />

    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Run"
        android:id="@+id/runButton"
        android:layout_alignParentBottom="true"
        android:layout_alignParentStart="true" />

</RelativeLayout>

----------------------------------------------------------------------- */


/* ------------------------- Activity File -----------------------------------------------------
All of this has been written wrt to an Activity. Modify as needed when you extend Activity.
public class SpeedGauge {

    Button runButton;
    ImageView needle;

    private Matrix matrix;
    private int framePerSeconds = 100;
    private long animationDuration = 10000;
    private long startTime;
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

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
------------------------------------------------------------------------------------------------*/