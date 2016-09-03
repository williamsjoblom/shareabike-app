package com.shareabike.shareabike;

import android.location.Location;

/**
 * Created by wax on 9/3/16.
 */
public class GPSManager {
    private static GPSManager instance;

    public static GPSManager getInstance() {
        if (instance == null)
            instance = new GPSManager();
        return instance;
    }

    public Location getLocation() {
        return null;
    }
}
