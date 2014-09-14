package com.example.cmpe243.googlemapstest;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Create by Asif N, Updated on 09/15/14.
 *
 */
public class HttpConnection extends AsyncTask<String,Void,Void>{


    HttpURLConnection httpURLConnection;
    BufferedReader input;
    StringBuilder response = new StringBuilder(1024);
    GoogleMap map;
    String jsonOutput;

    public static final String MAIN_TAG = "Internet Connection";


    HttpConnection(GoogleMap map){
        this.map = map;
    }

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
        jsonOutput = response.toString();
        Log.v("1st Log: " + MAIN_TAG, jsonOutput);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        JsonParsing jsonParsing = new JsonParsing(map);
        jsonParsing.execute(jsonOutput);
    }


}
