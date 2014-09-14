package com.example.cmpe243.googlemapstest;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Asif N on 09/09/2014.
 */

public class JsonParsing extends AsyncTask<String,Void,Void>{

    public static final String MAIN_TAG = "JSON Parsing";
    static int arrayLength;
    static LatLng[] latLngs = new LatLng[6];
    static LatLng [] midPoints = new LatLng[6];

    public static GoogleMap map;

    JsonParsing(GoogleMap map)
    {
        this.map = map;
    }

    @Override
    protected Void doInBackground(String... strings) {
        int i = 0;
        JSONObject start = null;
        JSONObject endLocation = null;
        try {
            JSONObject jsonObject = new JSONObject(strings[0]);
            JSONArray jsonArray = jsonObject.getJSONArray("routes");
            JSONObject route = jsonArray.getJSONObject(0);
            JSONArray jsonArray1 = route.getJSONArray("legs");
            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
            JSONArray jsonArray2 = jsonObject1.getJSONArray("steps");
            arrayLength = jsonArray2.length();
            String logs = jsonArray2.toString();
            Log.v("2nd Log: " + MAIN_TAG, logs);
            Log.v(MAIN_TAG, "Number of steps: " + arrayLength);
            for (i = 0; i < arrayLength; i++) {
                JSONObject step = jsonArray2.getJSONObject(i);
                start = step.getJSONObject("start_location");
                endLocation = step.getJSONObject("end_location");
                Double startLat = start.getDouble("lat");
                Double startLng = start.getDouble("lng");
                latLngs[i] = new LatLng(startLat, startLng);
                Log.v("Start Location: \n" + "Lat: " + startLat + "\nLng: " + startLng, "");
            }
            Log.v(MAIN_TAG, "Value of i: " + i);
            if(i == arrayLength){
                Double endLat = endLocation.getDouble("lat");
                Double endLng = endLocation.getDouble("lng");
                latLngs[i] = new LatLng(endLat, endLng);
                Log.v("End Location: \n" + "Lat: " + endLat + "\nLng: " + endLng, "");
            }
            for(i=0; i< arrayLength; i++)   {
                Log.v(MAIN_TAG, "Array Length: " + latLngs.length);
                double startLat = latLngs[i].latitude;
                Log.v(MAIN_TAG, "" + startLat);
                double startLong = latLngs[i].longitude;
                double endLat = latLngs[i + 1].latitude;
                double endLong = latLngs[i + 1].longitude;
                midPoints[i] = new LatLng((startLat + endLat) / 2, (startLong + endLong) / 2);
                Log.v(MAIN_TAG, "MidPoints: " + midPoints[i].latitude + " " + midPoints[i].longitude);
            }
            Log.d(MAIN_TAG, "Final Point: " + latLngs[arrayLength].latitude);
            Log.d(MAIN_TAG, "Endpoint: " + midPoints[arrayLength-1].latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (i = 0; i < arrayLength; i++) {
            Log.d(MAIN_TAG, "" + midPoints[i].latitude + " " + midPoints[i].longitude);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        DrawLinesOnMap drawLines = new DrawLinesOnMap();
        drawLines.drawConnectingMarkers();
    }
}
