//package com.example.cmpe243.googlemapstest;
//
///**
// * Created by jayanthnallapothula on 10/21/14.
// */
//import android.app.Activity;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothServerSocket;
//import android.bluetooth.BluetoothSocket;
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//import android.widget.Toast;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.Set;
//import java.util.UUID;
//
//public class BluetoothConnection extends Thread {
//
//    BluetoothAdapter mBluetoothAdapter;
//    BluetoothSocket mBluetoothSocket =  null;
//    public static final String MAINACTIVITY_TAG = "MAIN_ACTIVITY";
//
//    InputStream mmInputStream;
//    public final static int REQ_BLUETOOTH_EN = 2;
//
//
//    int readBufferedPosition = 0;
//    byte [] readBuffer;
//    volatile boolean stopWorker;
//    Thread workerThread;
//
//    Activity activity;
//    Context context;
//    BluetoothConnection(Activity activity, Context context){
//        this.activity = activity;
//        this.context = context;
//    }
//
//    boolean getBluetoothConnection(){
//
//        ArrayList<BluetoothDevice> deviceArrayList = new ArrayList<BluetoothDevice>();
//        boolean connectionEstablised = false;
//
//        final UUID WELL_KNOW_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//        // To check if Bluetooth is enabled
//        if(!(mBluetoothAdapter.isEnabled())){
//            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            activity.startActivityForResult(turnOn, REQ_BLUETOOTH_EN);
//        } else {
//
//            Toast.makeText(context, "Bluetooth Already ON", Toast.LENGTH_LONG).show();
//
//        }
//
//        Intent discoverable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//
//        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//        if(pairedDevices.size() > 0){
//            for(BluetoothDevice device : pairedDevices){
//                Log.d(MAINACTIVITY_TAG + "Device", device.getUuids().toString() + "\n" + device.getAddress());
//                deviceArrayList.add(device);
//            }
//        }
//        BluetoothDevice ourDevice = deviceArrayList.get(0);
//        Log.e(MAINACTIVITY_TAG, ourDevice.getAddress().toString());
//        try {
//            mBluetoothSocket = ourDevice.createRfcommSocketToServiceRecord(WELL_KNOW_UUID);
//            Log.e(MAINACTIVITY_TAG + "", WELL_KNOW_UUID.toString());
//            Method method = ourDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
//
//        }catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Log.i(MAINACTIVITY_TAG, "Trying to connect.......");
//        mBluetoothAdapter.cancelDiscovery();
//
//        try {
//            mBluetoothSocket.connect();
//            Log.i(MAINACTIVITY_TAG, "Connection established");
//            connectionEstablised = true;
//        } catch (IOException e) {
//            Log.e(MAINACTIVITY_TAG, "Could not connect!!!!!");
//            e.printStackTrace();
//        }
//        Boolean isConnected = mBluetoothSocket.isConnected();
//        if(isConnected){
//            Log.e(MAINACTIVITY_TAG, "BLUETOOTH CONNECTED");
//        }
//
//        ConnectedThread connectedThread =  new ConnectedThread(mBluetoothSocket);
//        connectedThread.start();
////        BluetoothSend sendData = new BluetoothSend(mBluetoothSocket);
////        sendData.execute();
//        if(connectionEstablised)
//            listenForData();
//        return connectionEstablised;
//
//    }
//
//    public void startThread(){
//        ConnectedThread connectedThread =  new ConnectedThread(mBluetoothSocket);
//        connectedThread.start();
//
////        BluetoothSend sendData = new BluetoothSend(mBluetoothSocket);
////        sendData.execute();
//    }
//
//    synchronized void listenForData(){
//
//        stopWorker = false;
//        readBufferedPosition = 0;
//        readBuffer = new byte[1024];
//        final byte delimiter = (byte)'$';
//
//        workerThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(!Thread.currentThread().isInterrupted() && !stopWorker){
//                    try{
//                        int bytesAvailable = mmInputStream.available();
//                        //Log.i(MAINACTIVITY_TAG, "Bytes Available : " + bytesAvailable);
//                        if(bytesAvailable > 0){
//                            byte [] packetBytes = new byte[bytesAvailable];
//                            int available = mmInputStream.read(packetBytes);
//                            Log.e(MAINACTIVITY_TAG, "Received: " + available);
////                            final String data = new String(packetBytes, "US-ASCII");
////                            Log.e(MAINACTIVITY_TAG, "Data: " + data);
//                            for(int i=0; i<bytesAvailable; i++){
//                                byte b = packetBytes[i];
//                                if(b==delimiter){
//                                    byte [] encodedBytes = new byte[readBufferedPosition];
//                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
//                                    final String data = new String(encodedBytes, "US-ASCII");
//                                    Log.e(MAINACTIVITY_TAG + "Received:", data);
//
//                                }
//                                else{
//                                    readBuffer[readBufferedPosition++] = b;
//                                }
//                            }
//                        }
//                    } catch (IOException e){
//                        Log.e(MAINACTIVITY_TAG, "Cannot read bytes");
//                        stopWorker = true;
//                    }
//                }
//            }
//        });
//        workerThread.start();
//    }
//
//    private class ConnectedThread extends Thread {
//
//        private final OutputStream mmOutputStream;
//
//        public ConnectedThread(BluetoothSocket socket){
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
//
//            try{
//                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
//            } catch (IOException e){
//                e.printStackTrace();
//            }
//
//            mmInputStream = tmpIn;
//            mmOutputStream= tmpOut;
//        }
//
////        public void run(){
////
////            //String stringBuffer = ActivityMain.sendDataText.getText().toString() + "\0";
////            //String stringBuffer = "5Hello World\0";      // Test String
////            //char stringBuffer = 'j';      // Test String
////            //byte [] buffer = sendBuffer.getBytes();
////            //byte buffer = (byte)stringBuffer;
////            try{
////
////                mmOutputStream.write(buffer);
////                mmOutputStream.flush();
////                Log.e(MAINACTIVITY_TAG, "Data send successful");
////            } catch (IOException e){
////                Log.e(MAINACTIVITY_TAG, "Error data send");
////            }
//
//        }
//
//    }
//
//}
