package com.shareabike.shareabike;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shareabike.shareabike.API.BorrowTask;
import com.shareabike.shareabike.API.GetBikeTask;
import com.shareabike.shareabike.API.GetBorrowedTask;
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

        //////// UGLY CODE AHEAD, BEWARE!!!!
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

                final Button borrowButton = (Button) findViewById(R.id.borrow_button);

                if (bike.getOwner() != MainActivity.USER_ID) {
                    if (bike.getRentedBy() != 0 && bike.getRentedBy() != MainActivity.USER_ID) {
                        // Unavailable
                    } else if (bike.getRentedBy() == MainActivity.USER_ID) {
                        // Borrowed
                        setButtonReturn(borrowButton);
                    } else {
                        // Available
                        new GetBorrowedTask(MainActivity.USER_ID) {
                            @Override
                            protected void onPostExecute(Integer result) {
                                setButtonBorrow(borrowButton);
                                if(result > 0)
                                    borrowButton.setEnabled(false);
                            }
                        }.execute();
                    }

                }
            }
        }.execute();
    }

    private void setButtonBorrow(final Button borrowButton) {
        borrowButton.setText("Borrow");

        borrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BorrowTask(MainActivity.USER_ID, bike.getID(), true).execute();
                setButtonReturn(borrowButton);
            }
        });

        borrowButton.setVisibility(View.VISIBLE);
    }

    private void setButtonReturn(final Button borrowButton) {
        borrowButton.setText("Return");

        borrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BorrowTask(MainActivity.USER_ID, bike.getID(), false).execute();
                setButtonBorrow(borrowButton);
            }
        });

        borrowButton.setVisibility(View.VISIBLE);
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
