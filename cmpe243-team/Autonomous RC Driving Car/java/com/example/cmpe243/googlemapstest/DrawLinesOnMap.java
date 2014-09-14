package com.example.cmpe243.googlemapstest;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by Asif N on 09/09/2014.
 */


public class DrawLinesOnMap {

    GoogleMap map;
    int arrayLength;
    static LatLng[] carPositions;

    DrawLinesOnMap()
    {
        this.map = JsonParsing.map;
        this.arrayLength = JsonParsing.arrayLength;
    }

    void drawConnectingMarkers()
    {
        if(map != null){
            map.addMarker(new MarkerOptions().position(JsonParsing.latLngs[arrayLength]).title("END POINT").snippet("This is the end point"));
            for(int i=0; i<arrayLength; i++) {
                map.addMarker(new MarkerOptions().position(JsonParsing.latLngs[i]).title("START POINT").snippet("This is the start point"));

                if(i <= arrayLength-1){
                    try {
                        Polyline line = map.addPolyline(new PolylineOptions().add(JsonParsing.latLngs[i], JsonParsing.latLngs[i+1])
                                .color(Color.RED).width(5));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }

            }
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(JsonParsing.latLngs[0], 16));
            LatLng[] carPositions = {JsonParsing.latLngs[0], JsonParsing.midPoints[0], JsonParsing.latLngs[1], JsonParsing.midPoints[1],
                    JsonParsing.latLngs[2], JsonParsing.midPoints[2], JsonParsing.latLngs[3], JsonParsing.midPoints[3], JsonParsing.latLngs[4]};

            //Log.d("Draw Class", "Latlngs[4]" + JsonParsing.latLngs[4].latitude);
            //Log.d("Draw Class", "Midpoints[3]" + JsonParsing.midPoints[3].latitude);
            //Log.d("Draw Class", "Lenght: " + carPositions.length);

//            for(int i=0; i<carPositions.length; i++) {
//                SimulateCar simulateCarPosition = new SimulateCar(carPositions);
//                simulateCarPosition.execute(i);
//
//            }
        }
    }
}
