package com.shareabike.shareabike;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wax on 9/3/16.
 */
public class Bike {
    private int id;
    private int owner;
    private String name;
    private Location location;
    private Marker marker;

    public Bike(String name) {
        this.id = 0;
        this.owner = 0;
        this.name = name;

        // Placeholder location pointing at campus Valla.
        this.location = new Location(LocationManager.GPS_PROVIDER);
        this.location.setLatitude(58.3978364);
        this.location.setLongitude(15.5760072);
    }

    public Bike(String name, Location location) {
        this.id = 0;
        this.owner = 0;
        this.name = name;
        this.location = location;
    }

    public Bike(JSONObject o) {
        try {
            id = o.getInt("id");
            name = o.getString("bike_name");
            owner = o.getInt("owner");

            JSONObject positionJson = o.getJSONArray("positions").getJSONObject(0);

            // Placeholder location pointing at campus Valla.
            this.location = new Location(LocationManager.GPS_PROVIDER);
            this.location.setLatitude(positionJson.getDouble("lat"));
            this.location.setLongitude(positionJson.getDouble("lon"));

        } catch (JSONException e) {
            Log.e("wax", "JSON error");
        }
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public void move(Location to) {
        this.location = to;

        if (marker != null) {
            marker.setPosition(new LatLng(to.getLatitude(), to.getLongitude()));
        }
    }

    public String getName() {
        return name;
    }

    public float getDistance() {
        GPSManager manager = GPSManager.getInstance();
        return manager.getLocation().distanceTo(location);
    }

    public double getLat() {
        return location.getLatitude();
    }

    public double getLong() {
        return location.getLongitude();
    }
}
