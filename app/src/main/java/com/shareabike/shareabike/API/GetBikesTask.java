package com.shareabike.shareabike.API;

import com.shareabike.shareabike.Bike;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wax on 9/3/16.
 */
public abstract class GetBikesTask extends ScheduledAsyncTask<Void, Void, ArrayList<Bike>> {
    @Override
    protected ArrayList<Bike> doInBackground(Void... params) {
        String data = API.read("bikes");

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

        return bikes;
    }
}