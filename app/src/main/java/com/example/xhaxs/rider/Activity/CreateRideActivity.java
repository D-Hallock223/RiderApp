package com.example.xhaxs.rider.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xhaxs.rider.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class CreateRideActivity extends AppCompatActivity {

    private static final String LOG_CLASS = CreateRideActivity.class.getName();
    private static final String URL_AUTO_PLACE_CONST = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";
    private final Activity activity = this;

    private Button mNextButton;

    private Place fromLocationFinal;
    private Place toLocationFinal;

    private PlaceAutocompleteFragment fromPlaceAutoCompleteFragment;
    private PlaceAutocompleteFragment toPlaceAutoCompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride);

        fromLocationFinal = null;
        toLocationFinal = null;

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activity.setTitle("Create Ride");
        fromPlaceAutoCompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_from);
        toPlaceAutoCompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_to);

        mNextButton = findViewById(R.id.b_next_cr);

        fromPlaceAutoCompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                fromLocationFinal = place;
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(CreateRideActivity.this, status.toString(), Toast.LENGTH_LONG).show();
                toLocationFinal = null;
            }
        });

        toPlaceAutoCompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                toLocationFinal = place;
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(CreateRideActivity.this, status.toString(), Toast.LENGTH_LONG).show();
                fromLocationFinal = null;
            }
        });


        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateRideActivity.this, CreateRideOtherDetails.class);
                if(checkAvailData() == false){
                    Toast.makeText(CreateRideActivity.this, "Values not Selected", Toast.LENGTH_LONG);
                    return;
                }
                intent.putExtra("fromLocationDetails", fromLocationFinal.getId());
                intent.putExtra("toLocationDetails", toLocationFinal.getId());
                Log.i(LOG_CLASS, "-------------------GOT FROM ID" + fromLocationFinal.getId() + "-------------------------------------");
                Log.i(LOG_CLASS, "-------------------GOT TO ID" + toLocationFinal.getId() + "-------------------------------------");
                startActivity(intent);
            }
        });
    }

    boolean checkAvailData(){
        if(fromLocationFinal == null || toLocationFinal == null) return false;
        return true;
    }
}
