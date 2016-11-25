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
    public final static String SERVER_URL = "http://138.68.147.88/";
    private final static String API_URL = SERVER_URL + "api/";

    private static API instance;

    public static API getInstance() {
        if (instance == null)
            instance = new API();
        return instance;
    }

    private API() { }

    public static String read(String path) {
        try {
            URL url = new URL(API_URL + path);

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = in.readLine()) != null) {
                builder.append(line);
            }

            in.close();
            return builder.toString();
        } catch (Exception ex) {
            Log.e("wax", "API read error: " + ex.toString());
        }

        return null;
    }
}
