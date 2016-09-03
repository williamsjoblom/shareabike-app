package com.shareabike.shareabike;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.PolylineOptions;
import com.shareabike.shareabike.API.API;
import com.shareabike.shareabike.API.GetBikesTask;
import com.shareabike.shareabike.API.OnBikesCallback;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


import java.util.ArrayList;

/**
 * Created by wax on 9/3/16.
 */
public class BikeViewManager implements OnMapReadyCallback, OnBikesCallback, AdapterView.OnItemClickListener, OnMarkerClickListener {

    private static final float FOCUS_ZOOM = 17;

    private Activity context;

    private SupportMapFragment mapFragment;
    final private ListView listView;
    private SlidingUpPanelLayout slide;

    private GoogleMap map;

    private ArrayList<Bike> bikes;
    private BikeAdapter listAdapter;

    public BikeViewManager(Activity context, SlidingUpPanelLayout s, SupportMapFragment mf, ListView l) {
        this.context = context;

        this.mapFragment = mf;
        this.listView = l;
        this.slide = s;

        listView.setOnItemClickListener(this);

        final OnBikesCallback callback = this;

        new GetBikesTask() {
            @Override
            protected void onPostExecute(ArrayList<Bike> b) {
                map.clear();

                bikes = b;

                listAdapter = new BikeAdapter(listView.getContext(), bikes);
                listView.setAdapter(listAdapter);

                addMarkers();
            }
        }.execute();

        //API.getBikes(callback);
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

        addMarkers();
    }

    @Override
    public void onBikes(ArrayList<Bike> bikes) {
        map.clear();

        this.bikes = bikes;

        listAdapter = new BikeAdapter(listView.getContext(), bikes);
        listView.setAdapter(listAdapter);

        addMarkers();
    }

    private void addMarkers() {
        if (map == null || bikes == null)
            return;

        for (Bike bike : bikes) {
            addMarker(bike);
        }
    }

    private void addMarker(Bike bike) {
        addMarker(bike, false);
    }

    private void addMarker(Bike bike, boolean drawBikePath) {
        if (map == null)
            return;

        LatLng location = new LatLng(bike.getLat(), bike.getLong());
        MarkerOptions options = new MarkerOptions().position(location);

        Marker marker = map.addMarker(options);
        marker.setTag(bike);
        bike.setMarker(marker);

        if(drawBikePath)
            drawPath(bike);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (map == null) return;
        Bike bike = bikes.get(position);
        startBikeActivity(bike);
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
        map.clear();

        addMarker(bike);

        LatLng latlng = new LatLng(bike.getLat(), bike.getLong());
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, FOCUS_ZOOM);
        map.animateCamera(update);

        View view = context.findViewById(R.id.bike_selected_view);
        view.setVisibility(View.VISIBLE);

        TextView selectedBikeText = (TextView) context.findViewById(R.id.selected_bike_text);
        selectedBikeText.setText(bike.getName());

        slide.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        slide.setTouchEnabled(false);

        View clearSelected = context.findViewById(R.id.clear_selected_bike);
        clearSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unShowOnMap();
            }
        });
    }

    public void drawPath(Bike bike) {
        ArrayList<Location> locations = bike.getLocations();

        if(locations.size() < 2) return;

        PolylineOptions line = new PolylineOptions();

        LatLng p0 = new LatLng(locations.get(0).getLatitude(), locations.get(0).getLongitude());

        for(int i = 1; i < locations.size(); i++) {
            Location l1 = locations.get(i);
            LatLng p1 = new LatLng(l1.getLatitude(), l1.getLongitude());

            line.add(p0, p1);
            p0 = p1;
        }

        map.addPolyline(line);
    }

    public void unShowOnMap() {
        map.clear();

        View view = context.findViewById(R.id.bike_selected_view);
        view.setVisibility(View.GONE);
        slide.setTouchEnabled(true);

        addMarkers();
    }
}
