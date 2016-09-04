package com.shareabike.shareabike.API;

/**
 * Created by wax on 9/4/16.
 */
public class FindTask extends NiceAsyncTask<Void, Void> {

    private final int bikeID;

    public FindTask(int bikeID) {
        this.bikeID = bikeID;
    }

    @Override
    protected Void doInBackground(Object... params) {
        API.read("bike/" + bikeID +"/find");
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) { }
}
