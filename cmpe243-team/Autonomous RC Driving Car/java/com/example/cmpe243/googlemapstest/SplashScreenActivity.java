package com.example.cmpe243.googlemapstest;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Asif N on 09/09/2014.
 */


public class SplashScreenActivity extends Activity {

    ProgressBar bluetoothConnectionProgress;
    BluetoothSocket mBluetoothSocket =  null;
    public static boolean connected = false;

    TextView connect, teamName;
    ImageView bluetoothImage;

    public static Timer timer;
    public static boolean stopTimer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        connect = (TextView)findViewById(R.id.connectingTextView);
        teamName = (TextView)findViewById(R.id.teamTextView);
        bluetoothImage = (ImageView)findViewById(R.id.bluetoothImageView);

        bluetoothConnectionProgress = (ProgressBar)findViewById(R.id.progressBar);
        bluetoothConnectionProgress.setMax(100);

        timer = new Timer();

        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!connected)
                        {
                            BluetoothConnect connectBluetooth = new BluetoothConnect(mBluetoothSocket,
                                    SplashScreenActivity.this, SplashScreenActivity.this);
                            connected = connectBluetooth.getBluetoothConnection();
                        }
                        else {
                            bluetoothConnectionProgress.setVisibility(View.INVISIBLE);
                            bluetoothImage.setVisibility(View.VISIBLE);
                            connect.setText("Connected!");
                            BluetoothReceive bluetoothReceive = new BluetoothReceive();
                            bluetoothReceive.listenForData();
                            stopTimer = true;
                            Delay delay = new Delay();
                            delay.execute();
                        }
                    }
                });
            }
        };

        if(!connected)
            timer.schedule(timerTask, 500, 5000);
        else    {
            bluetoothConnectionProgress.setVisibility(View.INVISIBLE);
            bluetoothImage.setVisibility(View.VISIBLE);
            connect.setText("Connected!");
            Delay delay = new Delay();
            delay.execute();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash_screen, menu);
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

    public class Delay extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
                if(stopTimer) {
                    timer.cancel();
                    timer.purge();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            Intent intent = new Intent(SplashScreenActivity.this, ActivityMain.class);
            startActivity(intent);
        }
    }
}
