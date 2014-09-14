package com.example.cmpe243.googlemapstest;

import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Asif N - updated 08/09/2014.
 *
 */
public class BluetoothSend extends Thread{


    public static final String MAINACTIVITY_TAG = "Bluetooth Send";
    String sendBuffer;

    BluetoothSend(String buffer)
    {
        sendBuffer = buffer;
    }

    public void run(){

        //String stringBuffer = ActivityMain.sendDataText.getText().toString() + "\0";
        //String stringBuffer = "5Hello World\0";      // Test String
        //char stringBuffer = 'j';      // Test String
        //byte [] buffer = stringBuffer.getBytes();
        //byte buffer = (byte)stringBuffer;
        byte [] buffer = sendBuffer.getBytes();
        try{

            BluetoothConnect.mmOutputStream.write(buffer);
            BluetoothConnect.mmOutputStream.flush();
            Log.e(MAINACTIVITY_TAG, "Data send successful");
        } catch (IOException e){
            Log.e(MAINACTIVITY_TAG, "Error data send");
        }
    }
}
