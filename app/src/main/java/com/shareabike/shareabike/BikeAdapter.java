package com.shareabike.shareabike;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by wax on 9/3/16.
 */
public class BikeAdapter extends ArrayAdapter<Bike> {
    public BikeAdapter(Context context, ArrayList<Bike> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bike bike = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_bike, parent, false);
        }

        TextView nameText = (TextView) convertView.findViewById(R.id.bikeNameText);
        TextView distanceText = (TextView) convertView.findViewById(R.id.bikeDistanceText);
        ImageView bikeImage = (ImageView) convertView.findViewById(R.id.bike_img);

        nameText.setText(bike.getName());
        distanceText.setText(String.format(Locale.US, "%.1fkm", bike.getDistance() / 1000));

        if (!bike.getImageURL().isEmpty())
            Picasso.with(this.getContext()).load(bike.getImageURL()).into(bikeImage);

        return convertView;
    }
}
