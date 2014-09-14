package com.example.cmpe243.googlemapstest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Asif N on 09/09/2014.
 */
public class IncomingDataChecker extends AsyncTask<String,Void,Void> {

    public static final String MAINACTIVITY_TAG = "Incoming Data Checker";

    public static final String testString = "Test";
    public static final String speedString = "speed";
    public static final String sensorString = "s1";
    public static final String gpsString = "lat";

    public static final String speedDelimiter = "[=, ]+";
    public static final String sensorDelimiter = "[=, ]+";
    public static final String gpsDelimiter = "[=, ]+";

    public String [] speedToken;
    public String [] sensorToken;
    public String [] gpsToken;

    public static String s1 = null, s2 = null, s3 = null;

    public static int count = 0;

    public enum recData{
        speed,
        sensor,
        gps,
        test,
        unrecognized
    }



    @Override
    protected Void doInBackground(String... data) {

        count++;
        recData incomingData = recData.unrecognized;
        int i = 0;
        Log.d(MAINACTIVITY_TAG, data[0]);
        try {
            if(data[0].substring(0, testString.length()).equals(testString)){
                incomingData = recData.test;
            }   else if(data[0].substring(0, sensorString.length()).equals(sensorString)){
                incomingData = recData.sensor;
                sensorToken = data[0].split(sensorDelimiter);
            }   else if(data[0].substring(0, speedString.length()).equals(speedString)){
                incomingData = recData.speed;
                speedToken = data[0].split(speedDelimiter);
            }   else if(data[0].substring(0, gpsString.length()).equals(gpsString)){
                incomingData = recData.gps;
                gpsToken = data[0].split(gpsDelimiter);
            }   else {
                incomingData = recData.unrecognized;
            }
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        switch (incomingData)
        {
            case test:
                Log.d(MAINACTIVITY_TAG, "data is: " + testString);
                break;

            case speed:
                Log.d(MAINACTIVITY_TAG, "data is: " + "Speed!");
                for(i=0; i<speedToken.length; i++){
                    if(speedToken[i].equals("speed")) {
                        i++;
                        break;
                    }
                }
                String speedValue = speedToken[i];
                Log.d(MAINACTIVITY_TAG, "Speed is: " + speedValue);
                break;

            case gps:
                Log.d(MAINACTIVITY_TAG, "data is: " + "GPS!");
                int count = 0;
                for(i=0; i<gpsToken.length; i++) {
                    if (gpsToken[i].equals("lat")) {
                        i++;
                        count++;
                        Log.d(MAINACTIVITY_TAG, "Token = " + gpsToken[i] + " Count = " + count);
                        break;

                    }
                }
                String latValue = gpsToken[i];
                for(i=count;i<gpsToken.length; i++){
                    if(gpsToken[i].equals("long")){
                        i++;
                        Log.d(MAINACTIVITY_TAG, "Token = " + gpsToken[i] + " Count = " + count);
                        break;
                    }
                }
                String longValue = gpsToken[i];
                Log.d(MAINACTIVITY_TAG, "Latitude is: " + latValue + " , Longitude is: " + longValue);

                break;

            case sensor:

                Log.d(MAINACTIVITY_TAG, "data is: " + "Sensor!");
                for(i=0;i<sensorToken.length;i++){
                    if(sensorToken[i].equals("s1")){
                        i++;
                        s1 = sensorToken[i++];
                        s2 = sensorToken[i++];
                        s3 = sensorToken[i];
                        break;
                    }
                }
                Log.d(MAINACTIVITY_TAG, "Sensor values: s1="+s1+" s2="+s2+" s3="+s3);

                break;

            default:
                Log.d(MAINACTIVITY_TAG, "Un-recognized data");
                break;

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

    }
}
