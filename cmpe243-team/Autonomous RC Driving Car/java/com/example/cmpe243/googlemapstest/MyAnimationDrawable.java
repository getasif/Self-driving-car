package com.example.cmpe243.googlemapstest;

import android.graphics.drawable.AnimationDrawable;
import android.os.SystemClock;

/**
 * Created by Asif N on 09/09/2014.
 */

public class MyAnimationDrawable extends AnimationDrawable {

    private volatile int duration;//its volatile because another thread will update its value
    private int currentFrame;

    public MyAnimationDrawable() {
        currentFrame = 0;
    }

    @Override
    public void run() {
        int n = getNumberOfFrames();
        currentFrame++;
        if (currentFrame >= n) {
            currentFrame = 0;
        }
        selectDrawable(currentFrame);
        scheduleSelf(this, SystemClock.uptimeMillis() + duration);
    }

    public void setDuration(int duration)
    {
        this.duration = duration;
        unscheduleSelf(this);
        selectDrawable(currentFrame);
        scheduleSelf(this, SystemClock.uptimeMillis()+duration);
    }
}
