package com.shareabike.shareabike;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.shareabike.shareabike.API.LocationTask;

/**
 * Created by wax on 9/3/16.
 */
public class GPSManager implements LocationListener {

    private static GPSManager instance;

    private Location location;
    private LocationManager locationManager;

    public static GPSManager getInstance() {
        if (instance == null)
            instance = new GPSManager();
        return instance;
    }

    private GPSManager() { }

    public void onCreate(Activity context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 2, this);
    }

    public Location getLocation() {
        if (location == null)
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        new LocationTask(location).execute();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }
}
