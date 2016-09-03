package com.shareabike.shareabike.API;

import com.shareabike.shareabike.Bike;

import java.util.ArrayList;

/**
 * Created by wax on 9/3/16.
 */
public interface OnBikesCallback {
    void onBikes(ArrayList<Bike> bikes);
}
