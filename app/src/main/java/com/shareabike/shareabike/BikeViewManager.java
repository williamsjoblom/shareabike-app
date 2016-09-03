package com.shareabike.shareabike;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.shareabike.shareabike.API.API;
import com.shareabike.shareabike.API.OnBikesCallback;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


import java.util.ArrayList;

/**
 * Created by wax on 9/3/16.
 */
public class BikeViewManager implements OnMapReadyCallback, OnBikesCallback, AdapterView.OnItemClickListener, OnMarkerClickListener {

    private static final float FOCUS_ZOOM = 16;

    private Activity context;

    private SupportMapFragment mapFragment;
    private ListView listView;
    private SlidingUpPanelLayout slide;

    private GoogleMap map;

    private ArrayList<Bike> bikes;
    private BikeAdapter listAdapter;

    private boolean hasMarkers = false;

    public BikeViewManager(Activity context, SlidingUpPanelLayout slide, SupportMapFragment mapFragment, ListView listView) {
        this.context = context;

        this.mapFragment = mapFragment;
        this.listView = listView;
        this.slide = slide;

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
        map.setOnMarkerClickListener(this);

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

            Marker marker = map.addMarker(options);
            marker.setTag(bike);
            bike.setMarker(marker);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (map == null) return;

        Bike bike = bikes.get(position);

        startBikeActivity(bike);

        /*
        LatLng latlng = new LatLng(bike.getLat(), bike.getLong());
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, FOCUS_ZOOM);
        map.animateCamera(update);*/
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Bike bike = (Bike)marker.getTag();
        startBikeActivity(bike);
        return false;
    }

    public void startBikeActivity(Bike bike) {
        Intent intent = new Intent(context, BikeActivity.class);
        intent.putExtra("id", bike.getID());
        context.startActivity(intent);
    }

    public void showOnMap(Bike bike) {
        View view = context.findViewById(R.id.bike_selected_view);
        view.setVisibility(View.VISIBLE);
        slide.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }
}
