package com.shareabike.shareabike;

import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shareabike.shareabike.API.API;
import com.shareabike.shareabike.API.OnBikesCallback;

import java.util.ArrayList;

/**
 * Created by wax on 9/3/16.
 */
public class BikeManager implements OnMapReadyCallback, OnBikesCallback {

    private SupportMapFragment mapFragment;
    private ListView listView;
    private GoogleMap map;

    private ArrayList<Bike> bikes;
    private BikeAdapter listAdapter;


    public BikeManager(SupportMapFragment mapFragment, ListView listView) {
        this.mapFragment = mapFragment;
        this.listView = listView;

        API.getBikes(this);
    }

    public void onCreate() {
        mapFragment.getMapAsync(this);

        listAdapter = new BikeAdapter(listView.getContext(), bikes);
        listView.setAdapter(listAdapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setMyLocationEnabled(true);

        for (Bike bike : bikes) {
            LatLng location = new LatLng(bike.getLat(), bike.getLong());
            MarkerOptions options = new MarkerOptions().position(location);
            bike.setMarker(map.addMarker(options));
        }

    }

    @Override
    public void onBikes(ArrayList<Bike> bikes) {
        this.bikes = bikes;
    }
}
