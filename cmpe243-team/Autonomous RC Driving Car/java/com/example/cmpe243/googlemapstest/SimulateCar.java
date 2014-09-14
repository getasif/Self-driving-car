package com.example.cmpe243.googlemapstest;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Asif N on 09/09/2014.
 */

public class SimulateCar extends AsyncTask<Integer,Void,Integer> {

    public static final String MAIN_TAG = "Simulate Car";
    LatLng[] latLngs = new LatLng[9];
    int i = 0;
    int count =0;
    GoogleMap map;
    public static Marker[] markers = new Marker[9];
    Marker m;
    MarkerOptions markerOptions;

    public SimulateCar(LatLng[] latLng) {
        this.latLngs = latLng;
        Log.v(MAIN_TAG, "" + i);
        this.map = JsonParsing.map;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        map.addMarker(new MarkerOptions().position(latLngs[0]));

    }

    @Override
    protected Integer doInBackground(Integer...index) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count++;

        return index[0];
    }

    @Override
    protected void onPostExecute(Integer index) {
        markers[index] = map.addMarker(new MarkerOptions().position(latLngs[index]).icon(BitmapDescriptorFactory
                .fromResource(R.drawable.current_dot)).anchor(0.5f,0.5f));
        if(index > 0){
            markers[index-1].setVisible(false);
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs[index], 17));
    }
}
