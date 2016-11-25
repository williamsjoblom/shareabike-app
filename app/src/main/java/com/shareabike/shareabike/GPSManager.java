package com.shareabike.shareabike;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.shareabike.shareabike.API.LocationTask;

/**
 * Created by wax on 9/3/16.
 */
public class GPSManager implements LocationListener {

    private static GPSManager instance;

    private Location location;
    private LocationManager locationManager;
    private boolean initialized;

    public static GPSManager getInstance() {
        if (instance == null)
            instance = new GPSManager();
        return instance;
    }

    private GPSManager() {
        initialized = false;
    }

    public void onCreate(Activity context) {
        initialized = true;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        int permissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 2, this);
        } else {
            Toast.makeText(context, "GPS permission not granted.", Toast.LENGTH_SHORT);
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

    public Location getLocation() {
        if (locationManager == null) return null;

        if (location == null) {
            try {
                return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } catch (SecurityException ex) {
                return null;
            }
        }

        return location;
    }

    private boolean ensureLocationPermission(Context context) {
        int permissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
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
