package com.shareabike.shareabike;

import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.maps.SupportMapFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class MainActivity extends AppCompatActivity {

    public static final int USER_ID = 1; // ERIK!
    public static final boolean LOG_PICASSO = false;

    private static BikeViewManager bikeViewManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Picasso.with(this).setLoggingEnabled(LOG_PICASSO);

        Permission.requestGPS(this);

        // Required for network on main thread. Soon to be removed. (Or)
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        SlidingUpPanelLayout slide = (SlidingUpPanelLayout)  findViewById(R.id.sliding_layout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        StickyListHeadersListView bikeList = (StickyListHeadersListView) findViewById(R.id.bike_list);

        bikeViewManager = new BikeViewManager(this, slide, mapFragment, bikeList);
        bikeViewManager.onCreate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Permission.GPS_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GPSManager.getInstance().onCreate(this);
                    bikeViewManager.signalGpsReady();
                } else {
                    //TODO: Disable functionality depending on GPS.
                }
            }
        }
    }

    public static BikeViewManager getBikeViewManager() {
        return bikeViewManager;
    }
}
