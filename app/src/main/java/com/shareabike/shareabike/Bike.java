package com.shareabike.shareabike;

import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wax on 9/3/16.
 */
public class Bike implements Serializable {

    private int id;
    private int owner;
    private int rentedBy;
    private String name;
    private String imageURL;
    private ArrayList<Location> locations;
    private Marker marker;


    public Bike(JSONObject o) {
        locations = new ArrayList<>();

        try {
            id = o.getInt("id");
            name = o.getString("bike_name");
            owner = o.getInt("owner");
            imageURL = o.getString("image_url");

            Object re = o.get("rented_by");

            if(re instanceof Integer)
                rentedBy = (Integer) re;
            else
                rentedBy = 0;

            JSONArray positionsJson = o.getJSONArray("positions");

            for (int i = 0; i < positionsJson.length(); i++) {
                JSONObject positionJson = positionsJson.getJSONObject(i);

                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLatitude(positionJson.getDouble("lat"));
                location.setLongitude(positionJson.getDouble("lon"));

                locations.add(location);
            }
        } catch (JSONException e) {
            Log.e("wax", "JSON error!!! " + e.toString());
        }
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Marker getMarker() { return marker; }

    public String getName() {
        return name;
    }

    public int getOwner() { return owner; }

    public int getID() { return id; }

    public String getImageURL() { return imageURL; }

    public float getDistance() {
        GPSManager manager = GPSManager.getInstance();
        return manager.getLocation().distanceTo(getLocation());
    }

    public Location getLocation() {
        if (locations.isEmpty())
            return new Location(LocationManager.GPS_PROVIDER);

        return locations.get(0);
    }

    public ArrayList<Location> getLocations() { return locations; }

    public double getLat() {
        if (locations.isEmpty())
            return 0;

        return locations.get(0).getLatitude();
    }

    public double getLong() {
        if (locations.isEmpty())
            return 0;

        return locations.get(0).getLongitude();
    }

    public int getRentedBy() { return rentedBy; }
}
