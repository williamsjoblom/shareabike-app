package com.shareabike.shareabike.API;

import android.util.Log;

import com.shareabike.shareabike.Bike;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by wax on 9/3/16.
 */
public class API {

    private final static String API_URL = "http://138.68.129.101/api/";

    private static API instance;

    public static API getInstance() {
        if (instance == null)
            instance = new API();
        return instance;
    }

    private API() { }

    public static void read(String path, OnReadCallback callback) {
        try {
            URL url = new URL(API_URL + path);

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = in.readLine()) != null) {
                builder.append(line);
            }

            in.close();
            callback.onRead(builder.toString());
        } catch (Exception ex) {
            Log.e("wax", "API read error");
        }
    }

    public static void getBikes(final OnBikesCallback callback) {
        read("bikes", new OnReadCallback() {
            @Override
            public void onRead(String data) {
                ArrayList<Bike> bikes = new ArrayList<>();

                try {
                    JSONArray jsonBikes = new JSONArray(data);

                    for (int i = 0; i < jsonBikes.length(); i++) {
                        JSONObject o = jsonBikes.getJSONObject(i);
                        bikes.add(new Bike(o));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onBikes(bikes);
            }
        });
    }

    public static void getBike(int id, final OnBikeCallback callback) {
        read("bike/" + id, new OnReadCallback() {
            @Override
            public void onRead(String data) {
                try {
                    JSONObject jsonBike = new JSONObject(data);
                    callback.onBike(new Bike(jsonBike));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }





}
