package com.shareabike.shareabike;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.shareabike.shareabike.API.FindTask;
import com.shareabike.shareabike.API.GetBikesTask;
import com.shareabike.shareabike.API.GetPOITask;
import com.shareabike.shareabike.API.OnBikesCallback;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by wax on 9/3/16.
 */
public class BikeViewManager implements OnMapReadyCallback, OnBikesCallback, AdapterView.OnItemClickListener, OnMarkerClickListener {

    private static final float FOCUS_ZOOM = 17;
    private static final long UPDATE_INTERVAL = 7000;

    private Activity context;

    private SupportMapFragment mapFragment;
    final private StickyListHeadersListView listView;
    private SlidingUpPanelLayout slide;

    private GoogleMap map;
    private boolean gpsReady = false;

    private ArrayList<Bike> bikes;
    private BikeAdapter listAdapter;
    private ArrayList<Marker> bikeMarkers;
    private ArrayList<Polyline> bikePaths;

    private static BikeViewManager instance;

    public static BikeViewManager getInstance(Activity context, SlidingUpPanelLayout s, SupportMapFragment mf, StickyListHeadersListView l) {
        if(instance == null)
            instance = new BikeViewManager(context, s, mf, l);
        return instance;
    }

    public static BikeViewManager getInstance() {
        return instance;
    }

    private BikeViewManager(Activity context, SlidingUpPanelLayout s, SupportMapFragment mf, StickyListHeadersListView l) {
        this.context = context;

        this.mapFragment = mf;
        this.listView = l;
        this.slide = s;

        this.bikePaths = new ArrayList<>();

        //s.setScrollableView(l);
        s.setDragView(context.findViewById(R.id.drag_view));

        listView.setOnItemClickListener(this);

        new GetBikesTask() {
            @Override
            protected void onPostExecute(ArrayList<Bike> b) {
                bikes = b;

                addMarkers();

                listAdapter = new BikeAdapter(listView.getContext(), sortBikes(bikes));
                listView.setAdapter(listAdapter);
            }
        }.execute();
    }

    public void onCreate() {
        mapFragment.getMapAsync(this);
        scheduleUpdates();
    }

    private void scheduleUpdates() {
        final Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        new GetBikesTask() {
                            @Override
                            protected void onPostExecute(ArrayList<Bike> b) {
                                clearBikeMarkers();
                                bikes = b;
                                addMarkers();
                                //Log.d("wax", "Map updated");
                            }
                        }.execute();
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(timerTask, UPDATE_INTERVAL, UPDATE_INTERVAL);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (gpsReady) signalGpsReady();

        //map.setMinZoomPreference(11);
        map.setOnMarkerClickListener(this);

        if (Permission.checkGPS(context))
            map.setMyLocationEnabled(true);

        addMarkers();
        addPOIs();
    }

    public void signalGpsReady() {
        gpsReady = true;

        if (map != null) {
            Location l = GPSManager.getInstance().getLocation();
            LatLng latlng = new LatLng(l.getLatitude(), l.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, FOCUS_ZOOM);
            map.moveCamera(update);
        }
    }

    @Override
    public void onBikes(ArrayList<Bike> bikes) {
        clearBikeMarkers();

        this.bikes = bikes;

        listAdapter = new BikeAdapter(listView.getContext(), bikes);
        listView.setAdapter(listAdapter);

        addMarkers();
    }

    private void addMarkers() {
        bikeMarkers = new ArrayList<>();

        if (map == null || bikes == null)
            return;

        for (Bike bike : bikes) {
            addMarker(bike);
        }
    }

    private void clearBikeMarkers() {
        if (bikeMarkers == null) return;

        for (Marker m : bikeMarkers) {
            m.remove();
        }
    }

    private void clearBikePaths() {
        for(Polyline path : bikePaths) {
            path.remove();
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
        options.title(bike.getName());

        Marker marker = map.addMarker(options);
        marker.setTag(bike);
        bike.setMarker(marker);

        bikeMarkers.add(marker);

        if(drawBikePath)
            drawPath(bike);
    }

    private void addPOIs() {
        new GetPOITask() {
            @Override
            protected void onPostExecute(ArrayList<PointOfInterest> pointOfInterests) {
                for(PointOfInterest poi : pointOfInterests) {
                    MarkerOptions options = new MarkerOptions().position(poi.getPos());

                    if(poi.getType() == PointOfInterest.Type.PUMP) {
                        options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
                        options.title("Pump");
                    }

                    map.addMarker(options);
                }

                Log.d("wax", "Added " + pointOfInterests.size() + " points of interest");
            }
        }.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bike bike = listAdapter.getItem(position);
        startBikeActivity(bike);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Ugly way of cheking if this is a POI or bike marker.
        // Bike markers has tag data.
        if (marker.getTag() != null) {
            Bike bike = (Bike) marker.getTag();
            startBikeActivity(bike);
            return true;
        }

        return false;
    }

    public void startBikeActivity(Bike bike) {
        Intent intent = new Intent(context, BikeActivity.class);
        intent.putExtra("id", bike.getID());
        context.startActivity(intent);
    }

    public void showOnMap(final Bike bike) {
        clearBikeMarkers();
        clearBikePaths();

        addMarker(bike);
        drawPath(bike);

        LatLng latlng = new LatLng(bike.getLat(), bike.getLong());
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, FOCUS_ZOOM);
        map.animateCamera(update);

        View view = context.findViewById(R.id.bike_selected_view);
        view.setVisibility(View.VISIBLE);

        TextView selectedBikeText = (TextView) context.findViewById(R.id.selected_bike_text);
        selectedBikeText.setText(bike.getName());

        Button findButton = (Button) context.findViewById(R.id.find_button);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FindTask(bike.getID()).execute();
            }
        });

        slide.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        slide.setTouchEnabled(false);

        if (bike.getRentedBy() == MainActivity.userId || bike.getOwner() == MainActivity.userId)
            findButton.setVisibility(View.VISIBLE);
        else
            findButton.setVisibility(View.INVISIBLE);

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

        PolylineOptions line = new PolylineOptions();
        if(locations.size() < 2) return;

        LatLng p0 = new LatLng(locations.get(0).getLatitude(), locations.get(0).getLongitude());

        for(int i = 1; i < locations.size(); i++) {
            Location l1 = locations.get(i);
            LatLng p1 = new LatLng(l1.getLatitude(), l1.getLongitude());

            line.add(p0, p1);
            p0 = p1;
        }

        bikePaths.add(map.addPolyline(line));
    }

    public void unShowOnMap() {
        clearBikeMarkers();
        clearBikePaths();

        View view = context.findViewById(R.id.bike_selected_view);
        view.setVisibility(View.GONE);
        slide.setTouchEnabled(true);

        addMarkers();
    }

    private static ArrayList<Bike> sortBikes(ArrayList<Bike> bikes) {
        ArrayList<Bike> s = new ArrayList<>();

        for(Bike b : bikes) {
            if(b.isMyRentedBike())
                s.add(b);
        }

        for(Bike b : bikes) {
            if(b.isMyBike())
                s.add(b);
        }

        for(Bike b : bikes) {
            boolean mine = b.isMyBike() || b.isMyRentedBike();
            if(b.isAvailable() && !mine)
                s.add(b);
        }

        for(Bike b : bikes) {
            boolean mine = b.isMyBike() || b.isMyRentedBike();
            if(b.isOccupied() && !mine)
                s.add(b);
        }

        return s;
    }
}
