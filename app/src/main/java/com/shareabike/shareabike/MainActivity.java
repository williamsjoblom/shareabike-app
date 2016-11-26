package com.shareabike.shareabike;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.preference.Preference;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class MainActivity extends AppCompatActivity {

    public static final boolean LOG_PICASSO = false;
    public static final int LOGIN_REQUEST = 1;
    private static final boolean CLEAR_LOGIN = false;

    public static int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeActionBar();

        Picasso.with(this).setLoggingEnabled(LOG_PICASSO);

        Intent testActivityIntent = new Intent(this, TestActivity.class);
        //startActivity(testActivityIntent);

        // Required for network on main thread. Soon to be removed. (Or)
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        SlidingUpPanelLayout slide = (SlidingUpPanelLayout)  findViewById(R.id.sliding_layout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        StickyListHeadersListView bikeList = (StickyListHeadersListView) findViewById(R.id.bike_list);

        BikeViewManager.getInstance(this, slide, mapFragment, bikeList); // Construct instance.

        if(Permission.checkGPS(this)) gpsReady();
        else Permission.requestGPS(this);



        SharedPreferences preferences = this.getSharedPreferences("pref", MODE_PRIVATE);

        boolean signedIn = preferences.contains("user_id") && !CLEAR_LOGIN;
        if(!signedIn) {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST);
        } else {
            userId = preferences.getInt("user_id", -1);
            Log.d("wax", "Signed in with user_id: " + userId);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Permission.GPS_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("wax", "GPS permission granted");
                    gpsReady();
                } else {
                    //TODO: Disable functionality depending on GPS permission.
                    Log.d("wax", "GPS permission not granted");
                }
            }
        }
    }

    /**
     * Called upon completion of SignInActivity with RESULT_OK if login was successful.
     * User id passed in intent.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST) {
            if (resultCode == RESULT_OK) {
                userId = data.getIntExtra("user_id", -1);
                Log.d("wax", "Got user_id from SignInActivity: " + userId);
            } else {
                //TODO: Handle failed login.
            }
        }
    }

    private void gpsReady() {
        GPSManager.getInstance().onCreate(this);
        BikeViewManager.getInstance().onCreate();
        BikeViewManager.getInstance().signalGpsReady();
    }

    private void initializeActionBar() {
        this.getSupportActionBar().setDisplayShowCustomEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.title_layout, null);

        TextView titleText = (TextView) v.findViewById(R.id.title_text);
        titleText.setText(this.getTitle());
        Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/Lobster-Regular.ttf");
        titleText.setTypeface(tf);

        this.getSupportActionBar().setCustomView(v);
    }

}
