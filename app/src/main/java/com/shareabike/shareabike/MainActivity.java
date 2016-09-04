package com.shareabike.shareabike;

import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.maps.SupportMapFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MainActivity extends AppCompatActivity {

    public static final int USER_ID = 1; // ERIK!

    private static BikeViewManager bikeViewManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Required for network on main thread. Soon to be removed. (Or)
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        GPSManager.getInstance().onCreate(this);

        SlidingUpPanelLayout slide = (SlidingUpPanelLayout)  findViewById(R.id.sliding_layout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        ListView bikeList = (ListView) findViewById(R.id.bike_list);

        bikeViewManager = new BikeViewManager(this, slide, mapFragment, bikeList);
        bikeViewManager.onCreate();
    }

    public static BikeViewManager getBikeViewManager() {
        return bikeViewManager;
    }
}
