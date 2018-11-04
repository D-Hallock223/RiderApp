package com.example.xhaxs.rider.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.xhaxs.rider.CommonBottomNavigationHandle;
import com.example.xhaxs.rider.Datatype.UserSumData;
import com.example.xhaxs.rider.LogHandle;
import com.example.xhaxs.rider.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateRideActivity extends AppCompatActivity {

    private static final String LOG_CLASS = CreateRideActivity.class.getName();
    private static final String URL_AUTO_PLACE_CONST = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";
    private final Activity activity = this;

    private FirebaseUser mCurrentUser;

    private Button mNextButton;
    private BottomNavigationView mBottomNavigationView;

    private Place fromLocationFinal;
    private Place toLocationFinal;

    private PlaceAutocompleteFragment fromPlaceAutoCompleteFragment;
    private PlaceAutocompleteFragment toPlaceAutoCompleteFragment;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride);

        mBottomNavigationView = findViewById(R.id.bn_bottom_nav);
        mBottomNavigationView.setSelectedItemId(R.id.mi_create_ride);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                CommonBottomNavigationHandle.chooseSelectedActivity(CreateRideActivity.this, menuItem);
                return true;
            }
        });

        fromLocationFinal = null;
        toLocationFinal = null;

        mCurrentUser = LogHandle.checkLogin(FirebaseAuth.getInstance(), this);
        LogHandle.checkDetailsAdded(mCurrentUser, this);

        getSupportActionBar().setElevation(0);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activity.setTitle("Create Ride");
        fromPlaceAutoCompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_from);
        toPlaceAutoCompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_to);

        mNextButton = findViewById(R.id.b_next_cr);

        fromPlaceAutoCompleteFragment
                .setFilter(new AutocompleteFilter.Builder().setTypeFilter(Place.TYPE_COUNTRY).setCountry("IN").build());

        toPlaceAutoCompleteFragment
                .setFilter(new AutocompleteFilter.Builder().setTypeFilter(Place.TYPE_COUNTRY).setCountry("IN").build());

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
        return fromLocationFinal != null && toLocationFinal != null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_logout_btn:
                LogHandle.logout(FirebaseAuth.getInstance(), this);
                return true;
            case R.id.menu_my_profile:
                Intent intent = new Intent(this, ProfileViewActivity.class);
                intent.putExtra(ProfileViewActivity.PROFILER_STRING, new UserSumData(mCurrentUser.getUid(), mCurrentUser.getDisplayName(), mCurrentUser.getEmail()));
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }
}
