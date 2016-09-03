package com.shareabike.shareabike;

import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.shareabike.shareabike.API.API;
import com.shareabike.shareabike.API.OnBikesCallback;


import java.util.ArrayList;

/**
 * Created by wax on 9/3/16.
 */
public class BikeViewManager implements OnMapReadyCallback, OnBikesCallback, AdapterView.OnItemClickListener {

    private static final float FOCUS_ZOOM = 16;

    private SupportMapFragment mapFragment;
    private ListView listView;
    private GoogleMap map;

    private ArrayList<Bike> bikes;
    private BikeAdapter listAdapter;

    private boolean hasMarkers = false;

    public BikeViewManager(SupportMapFragment mapFragment, ListView listView) {
        this.mapFragment = mapFragment;
        this.listView = listView;

        listView.setOnItemClickListener(this);

        final OnBikesCallback callback = this;
        API.getBikes(callback);
    }

    public void onCreate() {
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setMinZoomPreference(11);

        Location l = GPSManager.getInstance().getLocation();
        LatLng latlng = new LatLng(l.getLatitude(), l.getLongitude());
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, FOCUS_ZOOM);
        map.moveCamera(update);

        map.setMyLocationEnabled(true);

        if (!hasMarkers)
            addMarkers();
    }

    @Override
    public void onBikes(ArrayList<Bike> bikes) {
        this.bikes = bikes;

        listAdapter = new BikeAdapter(listView.getContext(), bikes);
        listView.setAdapter(listAdapter);

        if (!hasMarkers)
            addMarkers();
    }

    private void addMarkers() {
        if (map == null || bikes == null)
            return;

        hasMarkers = true;

        for (Bike bike : bikes) {
            LatLng location = new LatLng(bike.getLat(), bike.getLong());
            MarkerOptions options = new MarkerOptions().position(location);
            bike.setMarker(map.addMarker(options));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (map == null) return;

        Bike bike = bikes.get(position);

        LatLng latlng = new LatLng(bike.getLat(), bike.getLong());
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, FOCUS_ZOOM);
        map.animateCamera(update);
    }

}
