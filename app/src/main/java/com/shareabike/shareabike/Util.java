package com.shareabike.shareabike;


/**
 * Created by wax on 9/3/16.
 */
public class Util {
    public static boolean equals(double d1, double d2) {
        final double EPS = 0.0001;
        double dd = d1 - d2;
        return Math.abs(dd) <= EPS;
    }
}
