package com.shareabike.shareabike;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * Created by wax on 9/3/16.
 */
public class DialogFactory {

    public static Dialog createBikeDialog(Activity context, Bike bike) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = context.getLayoutInflater();

        View view = inflater.inflate(R.layout.bike_dialog, null);
        builder.setView(view);

        ImageView imageView = (ImageView) view.findViewById(R.id.dialog_image);
        TextView nameText = (TextView) view.findViewById(R.id.bike_name);

        nameText.setText(bike.getName());
        if (!bike.getImageURL().isEmpty())
            Picasso.with(context).load(bike.getImageURL()).into(imageView);

        return builder.create();
    }

}
