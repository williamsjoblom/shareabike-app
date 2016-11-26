package com.shareabike.shareabike;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.shareabike.shareabike.API.API;
import com.shareabike.shareabike.API.BorrowTask;
import com.shareabike.shareabike.API.GetBikeTask;
import com.shareabike.shareabike.API.GetBorrowedTask;
import com.shareabike.shareabike.API.LockTask;
import com.squareup.picasso.Picasso;

public class BikeActivity extends AppCompatActivity {
    
    private int id;
    private Bike bike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike);
        //getWindow().setStatusBarColor(R.color.colorPrimaryDark);

        //ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), );

        CollapsingToolbarLayout collapsingLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        collapsingLayout.setContentScrimResource(R.color.colorPrimary);

        setSupportActionBar((Toolbar) findViewById(R.id.bike_toolbar));
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        //////// UGLY CODE AHEAD, BEWARE!!!!
        final Activity context = this;
        new GetBikeTask(id) {
            @Override
            protected void onPostExecute(Bike b) {
                bike = b;

                ImageView imageView = (ImageView) findViewById(R.id.dialog_image);
                TextView ownerText = (TextView) findViewById(R.id.bike_owner_text);
                actionBar.setTitle(bike.getName());
                ownerText.setText(bike.getOwnerName());

                if (!bike.getImageURL().isEmpty())
                    Picasso.with(context).load(API.SERVER_URL + bike.getImageURL()).into(imageView);

                final Button borrowButton = (Button) findViewById(R.id.borrow_button);
                final Button lockButton = (Button) findViewById(R.id.lock_button);

                lockButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bike.setLocked(!bike.isLocked());

                        if(bike.isLocked())
                            lockButton.setText("Unlock");
                        else
                            lockButton.setText("Lock");

                        new LockTask(bike.getID(), bike.isLocked()).execute();
                    }
                });

                if(bike.isLocked())
                    lockButton.setText("Unlock");
                else
                    lockButton.setText("Lock");

                if (bike.getOwner() != MainActivity.userId) {
                    if (bike.getRentedBy() != 0 && bike.getRentedBy() != MainActivity.userId) {
                        // Unavailable
                        borrowButton.setVisibility(View.VISIBLE);
                        borrowButton.setText("Occupied");
                        setButtonDisabled(borrowButton);
                    } else if (bike.getRentedBy() == MainActivity.userId) {
                        // The bike is borrowed to you!
                        borrowButton.setVisibility(View.VISIBLE);
                        setButtonReturn(borrowButton, lockButton);
                    } else {
                        // Available
                        new GetBorrowedTask(MainActivity.userId) {
                            @Override
                            protected void onPostExecute(Integer result) {
                                borrowButton.setVisibility(View.VISIBLE);
                                if(result > 0) {
                                    borrowButton.setText("You can't borrow more than one bike!");
                                    setButtonDisabled(borrowButton);
                                } else {
                                    setButtonBorrow(borrowButton, lockButton);
                                }
                            }
                        }.execute();
                    }
                } else {
                    if(bike.isLocked())
                        lockButton.setText("Unlock");
                    else
                        lockButton.setText("Lock");

                    lockButton.setVisibility(View.VISIBLE);
                    borrowButton.setVisibility(View.GONE);
                }
            }
        }.execute();
    }

    private void setButtonBorrow(final Button borrowButton, final Button lockButton) {
        borrowButton.setText("Borrow");

        lockButton.setVisibility(View.GONE);

        borrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BorrowTask(MainActivity.userId, bike.getID(), true).execute();
                setButtonReturn(borrowButton, lockButton);
            }
        });
    }

    private void setButtonReturn(final Button borrowButton, final Button lockButton) {
        borrowButton.setText("Return");

        lockButton.setVisibility(View.VISIBLE);

        borrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BorrowTask(MainActivity.userId, bike.getID(), false).execute();
                setButtonBorrow(borrowButton, lockButton);
            }
        });
    }

    private void setButtonDisabled(Button borrowButton) {
        borrowButton.setAlpha(0.5f);
        borrowButton.setEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.show_on_map:
                if (bike == null) return false;
                BikeViewManager.getInstance().showOnMap(bike);
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
