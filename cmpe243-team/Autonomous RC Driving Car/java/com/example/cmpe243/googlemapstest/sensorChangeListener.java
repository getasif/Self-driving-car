package com.example.cmpe243.googlemapstest;

import android.util.Log;

/**
 * Created by Asif N on 09/09/2014.
 */


public class sensorChangeListener {

    public static String sensor1;

    public interface Listener{
        public void onStateChange(boolean state);
    }

    private Listener mListener = null;
    public void registerListener(Listener listener){
        mListener = listener;
        sensor1 = IncomingDataChecker.s1;
    }

    public static boolean myBoolean = false;
    public void doYourWork(){
        Log.d("HAHA", sensor1 + " : " + IncomingDataChecker.s1);
        if(!(sensor1.equals(IncomingDataChecker.s1))){
            Log.d("HAHA", sensor1 + " : " + IncomingDataChecker.s1);
            if(mListener != null) {
                myBoolean = true;
                sensor1 = IncomingDataChecker.s1;
                this.mListener.onStateChange(myBoolean);
            }
        }
        else{
            myBoolean = false;
            this.mListener.onStateChange(myBoolean);
        }
    }

}
