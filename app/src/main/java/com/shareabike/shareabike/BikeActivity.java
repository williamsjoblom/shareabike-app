package com.shareabike.shareabike;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.shareabike.shareabike.API.GetBikeTask;
import com.squareup.picasso.Picasso;

public class BikeActivity extends AppCompatActivity {

    private int id;
    private Bike bike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

        final Activity context = this;
        new GetBikeTask(id) {
            @Override
            protected void onPostExecute(Bike b) {
                bike = b;

                ImageView imageView = (ImageView) findViewById(R.id.dialog_image);
                TextView nameText = (TextView) findViewById(R.id.bike_name);

                nameText.setText(bike.getName());
                if (!bike.getImageURL().isEmpty())
                    Picasso.with(context).load(bike.getImageURL()).into(imageView);
            }
        }.execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.show_on_map:
                if (bike == null) return false;
                MainActivity.getBikeViewManager().showOnMap(bike);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bike, menu);
        return true;
    }
}
