package com.shareabike.shareabike.API;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.shareabike.shareabike.PointOfInterest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wax on 9/4/16.
 */
public abstract class GetPOITask extends NiceAsyncTask<Void, ArrayList<PointOfInterest>> {
    @Override
    protected ArrayList<PointOfInterest> doInBackground(Object... params) {
        ArrayList<PointOfInterest> pois = new ArrayList<>();

        String data = API.read("points");
        try {
            JSONObject poisJson = new JSONObject(data);

            JSONArray pumpsJson = poisJson.getJSONArray("pumps");

            for(int i = 0; i < pumpsJson.length(); i++) {
                JSONArray pumpJson = pumpsJson.getJSONArray(i);
                LatLng pos = new LatLng(pumpJson.getDouble(1), pumpJson.getDouble(0));
                pois.add(new PointOfInterest(PointOfInterest.Type.PUMP, pos));
            }
        } catch (Exception e) {
            Log.e("wax", "JSON error : " + e.toString());
        }

        return pois;
    }
}
