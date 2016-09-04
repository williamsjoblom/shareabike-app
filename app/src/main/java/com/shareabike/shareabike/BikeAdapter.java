package com.shareabike.shareabike;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by wax on 9/3/16.
 */
public class BikeAdapter extends ArrayAdapter<Bike> implements StickyListHeadersAdapter {

    public BikeAdapter(Context context, ArrayList<Bike> bikes) {
        super(context, 0, bikes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bike bike = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_bike, parent, false);
        }

        TextView nameText = (TextView) convertView.findViewById(R.id.bike_name_text);
        TextView distanceText = (TextView) convertView.findViewById(R.id.bike_distance_text);
        TextView ownerText = (TextView) convertView.findViewById(R.id.bike_owner_text);
        ImageView bikeImage = (ImageView) convertView.findViewById(R.id.bike_img);

        nameText.setText(bike.getName());
        distanceText.setText(String.format(Locale.US, "%.1fkm", bike.getDistance() / 1000));
        ownerText.setText(bike.getOwnerName());

        if (!bike.getImageURL().isEmpty())
            Picasso.with(this.getContext()).load(bike.getImageURL()).into(bikeImage);

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_header, parent, false);

        TextView headerText = (TextView) convertView.findViewById(R.id.header_text);
        headerText.setText(getHeaderTitle(position));

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        Bike b = getItem(position);
        return b.getHeaderId();
    }

    public String getHeaderTitle(int position) {
        Bike b = getItem(position);
        switch (b.getHeaderId()) {
            case Bike.RENTED:
                return "BORROWED";
            case Bike.OWNED:
                return "OWNED";
            case Bike.NEARBY:
                return "NEARBY";
            case Bike.OCCUPIED:
                return "OCCUPIED";
        }

        Log.e("wax", "HEADER ID" + position);

        return "unknown";
    }


}
