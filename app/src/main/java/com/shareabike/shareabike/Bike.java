package com.shareabike.shareabike;

import android.location.Location;

/**
 * Created by wax on 9/3/16.
 */
public class Bike {
    private String name;
    private Location location;

    public String getName() {
        return name;
    }

    public float getDistance() {
        GPSManager manager = GPSManager.getInstance();
        return manager.getLocation().distanceTo(location);
    }
}
