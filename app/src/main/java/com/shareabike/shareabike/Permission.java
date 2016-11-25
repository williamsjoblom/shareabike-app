package com.shareabike.shareabike;

import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by wax on 11/25/16.
 */
public class Permission {

    public final static int GPS_PERMISSION = 1;

    public static boolean checkGPS(Context context) {
        return check(context, android.Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public static void requestGPS(Activity activity) {
        if(check(activity, android.Manifest.permission.ACCESS_FINE_LOCATION))
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            //TODO: Show permission explanation.
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION },
                    GPS_PERMISSION);
        }
    }

    public static boolean check(Context context, String permission) {
        int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }
}
