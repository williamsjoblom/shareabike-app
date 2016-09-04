package com.shareabike.shareabike.API;

import android.util.Log;

import com.shareabike.shareabike.Bike;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wax on 9/3/16.
 */
public abstract class GetBikesTask extends NiceAsyncTask<Void, ArrayList<Bike>> {
    @Override
    protected ArrayList<Bike> doInBackground(Object... params) {
        String data = API.read("bikes");

        ArrayList<Bike> bikes = new ArrayList<>();
        try {
            JSONArray jsonBikes = new JSONArray(data);

            for (int i = 0; i < jsonBikes.length(); i++) {
                JSONObject o = jsonBikes.getJSONObject(i);
                bikes.add(new Bike(o));
            }
        } catch (Exception e) {
            Log.e("wax", "JSON err: " + e.toString());
        }

        return bikes;
    }
}