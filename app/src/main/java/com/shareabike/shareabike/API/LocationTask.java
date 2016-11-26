package com.shareabike.shareabike.API;

import android.location.Location;

import com.shareabike.shareabike.MainActivity;

/**
 * Created by wax on 9/4/16.
 */
public class LocationTask extends NiceAsyncTask<Void, Void> {

    private final Location location;

    public LocationTask(Location location) {
        this.location = location;
    }

    @Override
    protected Void doInBackground(Object... params) {
        //"http://138.68.129.101/api/user/1/report/58.394628,15.560919"
        API.read("user/" + MainActivity.userId + "/report/" + location.getLatitude() + "," +
                location.getLongitude());
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) { }
}
