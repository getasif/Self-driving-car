package com.example.cmpe243.googlemapstest;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Asif N on 09/09/2014.
 */

public class FreeRunActivity extends Activity implements SensorEventListener{

    TextView xTextView, yTextView, zTextView;
    TextView xValTextView, yValTextView, zValTextView;
    TextView direction;
    Button startButton;

    private Sensor mAccelerometer;
    private Sensor magnetometer;

    Vibrator v;

    private static final float ALPHA = 0.2f;
    public static final String LEFT = "LEFT TURN!";
    public static final String RIGHT = "RIGHT TURN!";
    public static final String ACCELERATE = "ACCELERATE!";
    public static final String BRAKE = "BRAKE!";

    public static final double ACCELERATE_THRESHOLD = 1.3;
    public static final double BRAKE_THRESHOLD = -2;
    public static final double LEFT_THRESHOLD = 1;
    public static final double RIGHT_THRESHOLD = -1;

    float smoothedValue;
    SensorManager mSensorManager;
    Sensor mSensor;

    GestureDetector gestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_run);

        xTextView = (TextView)findViewById(R.id.xTextView);
        yTextView = (TextView)findViewById(R.id.yTextView);
        zTextView = (TextView)findViewById(R.id.zTextView);

        xValTextView = (TextView)findViewById(R.id.xValueTextView);
        yValTextView = (TextView)findViewById(R.id.yValueTextView);
        zValTextView = (TextView)findViewById(R.id.zValueTextView);
        direction = (TextView)findViewById(R.id.directionTextView);

        xValTextView.setTextSize(15);
        yValTextView.setTextSize(15);
        zValTextView.setTextSize(15);
        direction.setTextSize(40);

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);

        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);

        gestureDetector = new GestureDetector(new SwipeGestureDetector(this, DashboardActivity.class, LedBlinkActivity.class));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private static float smooth(float input, float output) {

        return (int) (output + ALPHA * (input - output));
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float yaw = sensorEvent.values[1];
        float tilt = sensorEvent.values[0];
        float pitch = sensorEvent.values[2];
        String setText = null;

        if(tilt > LEFT_THRESHOLD){
            setText = LEFT;
        }   else if(tilt < RIGHT_THRESHOLD)    {
            setText = RIGHT;
        }   else if(pitch > ACCELERATE_THRESHOLD){
            setText = ACCELERATE;
        }   else if(pitch < BRAKE_THRESHOLD) {
            setText = BRAKE;
        }   else    {
            setText = "";
        }
        direction.setText(setText);

        smoothedValue = smooth(yaw, smoothedValue);
        yaw = smoothedValue;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.free_run, menu);
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
