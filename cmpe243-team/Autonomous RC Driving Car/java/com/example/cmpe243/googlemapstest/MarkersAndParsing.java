package com.example.cmpe243.googlemapstest;

/**
 * Created by jayanthnallapothula on 10/21/14.
 */
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Asif N on 09/09/2014.
 */

public class MarkersAndParsing {


    Marker[] markers = new Marker[7];
    public static final String MAIN_TAG = "JSON Parsing ";
    GoogleMap map;
    URL url;
    String stringUrl = "http://maps.googleapis.com/maps/api/directions/json?origin=37.335982,-121.88" +
            "0960&destination=37.333576,-121.878582&sensor=false&mode=walking";

    public MarkersAndParsing(GoogleMap map) {
        this.map = map;
    }

    void callOtherTasks(){
        RetrieveFeedTask retrieveFeedTask = new RetrieveFeedTask();
        retrieveFeedTask.execute(stringUrl);
    }


    public class RetrieveFeedTask extends AsyncTask<String,Void,Void> {

        HttpURLConnection httpURLConnection;
        BufferedReader input;
        StringBuilder response = new StringBuilder(1024);
        int arrayLength;
        LatLng[] latLngs = new LatLng[4];
        LatLng [] midPoints = new LatLng[4];


        @Override
        protected Void doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    input = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()), 8192);
                    String strLine = null;
                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
            String jsonOutput = response.toString();
            Log.v("1st Log: " + MAIN_TAG, jsonOutput);
            try {
                JSONObject jsonObject = new JSONObject(jsonOutput);
                JSONArray jsonArray = jsonObject.getJSONArray("routes");
                JSONObject route = jsonArray.getJSONObject(0);
                JSONArray jsonArray1 = route.getJSONArray("legs");
                JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                JSONArray jsonArray2 = jsonObject1.getJSONArray("steps");
                arrayLength = jsonArray2.length();
                String logs = jsonArray2.toString();
                Log.v("2nd Log: " + MAIN_TAG, logs);
                for(int i=0; i<arrayLength; i++){
                    JSONObject step = jsonArray2.getJSONObject(i);
                    JSONObject start = step.getJSONObject("start_location");
                    Double startLat = start.getDouble("lat");
                    Double startLng = start.getDouble("lng");
                    latLngs[i] = new LatLng(startLat, startLng);
                    Log.v("Start Location: \n" + "Lat: " + startLat + "\nLng: " + startLng, "");
                }
                for(int i=0; i<latLngs.length-1; i++){
                    Log.v(MAIN_TAG,"Array Length: " + latLngs.length);
                    double startLat = latLngs[i].latitude;
                    Log.v(MAIN_TAG, ""+startLat);
                    double startLong = latLngs[i].longitude;
                    double endLat = latLngs[i+1].latitude;
                    double endLong = latLngs[i+1].longitude;
                    midPoints[i] = new LatLng((startLat + endLat) / 2, (startLong + endLong) / 2);
                    Log.v(MAIN_TAG, ""+ midPoints[i].latitude + " " + midPoints[i].longitude);

                }
            } catch (JSONException e) {
                e.printStackTrace();

            }

            for(int i=0; i<midPoints.length-1; i++){
                Log.d(MAIN_TAG, "" + midPoints[i].latitude + " " + midPoints[i].longitude);
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            if(map != null){
                for(int i=0; i<arrayLength; i++) {
                    map.addMarker(new MarkerOptions().position(latLngs[i]).title("START POINT").snippet("This is the start point"));

                    if(i <= arrayLength-2){
                        try {
                            Polyline line = map.addPolyline(new PolylineOptions().add(latLngs[i], latLngs[i+1])
                                    .color(Color.RED).width(5));
                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    }

                }
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs[0], 15));
                LatLng [] carPositions = {latLngs[0], midPoints[0], latLngs[1], midPoints[1],
                        latLngs[2], midPoints[2], latLngs[3]};


                for(int i=0; i<carPositions.length; i++) {
                    SimulateCarPosition simulateCarPosition = new SimulateCarPosition(carPositions, i);
                    simulateCarPosition.execute(map);
                }
            }
        }
    }

    public class SimulateCarPosition extends AsyncTask<GoogleMap,Void,Void>{

        LatLng [] latLngs = new LatLng[7];
        int i = 0;
        int count =0;

        public SimulateCarPosition(LatLng[] latLng, int index) {
            this.latLngs = latLng;
            this.i = index;
            Log.v(MAIN_TAG, ""+i);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            map.addMarker(new MarkerOptions().position(latLngs[0]));
        }

        @Override
        protected Void doInBackground(GoogleMap... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count++;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            if(i < 3) {
                markers[i] = map.addMarker(new MarkerOptions().position(latLngs[i]).
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.car_clipart)));

            }
            else if(i >= 3 && i < 5){
                markers[i] = map.addMarker(new MarkerOptions().position(latLngs[i]).icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.car_clipart)).rotation(270));
            }
            else{
                markers[i] = map.addMarker(new MarkerOptions().position(latLngs[i]).icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.car_clipart)).rotation(180));
            }
            if(i > 0){
                markers[i-1].remove();
            }
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs[i], 17));
            //Toast.makeText(MainActivity.this, "" + count, Toast.LENGTH_LONG).show();
        }
    }
}


