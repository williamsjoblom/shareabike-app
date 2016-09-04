package com.shareabike.shareabike;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by wax on 9/4/16.
 */
public class PointOfInterest {
    public enum Type {
        PUMP
    }

    private Type type;
    private LatLng pos;

    public PointOfInterest(Type type, LatLng pos) {
        this.type = type;
        this.pos = pos;
    }

    public Type getType() {
        return type;
    }

    public LatLng getPos() {
        return pos;
    }
}
