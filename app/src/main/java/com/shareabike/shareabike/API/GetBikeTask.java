package com.shareabike.shareabike.API;

import com.shareabike.shareabike.Bike;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wax on 9/3/16.
 */
public abstract class GetBikeTask extends NiceAsyncTask<Void, Bike> {

    private final int id;

    public GetBikeTask(int id) {
        this.id = id;
    }

    @Override
    protected Bike doInBackground(Object... params) {
        try {
            String data = API.read("bike/" + id);
            JSONObject jsonBike = new JSONObject(data);
            return new Bike(jsonBike);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
