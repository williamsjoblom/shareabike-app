package com.shareabike.shareabike;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Required for network on main thread. Soon to be removed.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        GPSManager.getInstance().onCreate(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        ListView bikeList = (ListView) findViewById(R.id.bike_list);

        BikeViewManager bikeViewManager = new BikeViewManager(mapFragment, bikeList);
        bikeViewManager.onCreate();
    }
}
