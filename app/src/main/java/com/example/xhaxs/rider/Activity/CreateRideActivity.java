package com.example.xhaxs.rider.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xhaxs.rider.Adapter.CRAutoAdapter;
import com.example.xhaxs.rider.AsyncTasks.PlaceTask;
import com.example.xhaxs.rider.Datatype.PlaceData;
import com.example.xhaxs.rider.R;

public class CreateRideActivity extends AppCompatActivity {

    private static final String LOG_CLASS = CreateRideActivity.class.getName();
    private static final String URL_AUTO_PLACE_CONST = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";
    private final Activity activity = this;
    private EditText mETFromLocation;
    private EditText mETToLocation;
    private RecyclerView mRVAutoComplete;
    private CRAutoAdapter mAdapterAutoComplete;
    private RecyclerView.LayoutManager mLayoutManagerAutoComplete;
    private Button mNextButton;
    private PlaceTask mPlaceTask;
    private PlaceData[] mPlaceData;
    private PlaceData fromLocationFinal;
    private PlaceData toLocationFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activity.setTitle("Create Ride");
        mETFromLocation = findViewById(R.id.et_create_ride_from_location);
        mETToLocation = findViewById(R.id.et_create_ride_to_location);
        mNextButton = findViewById(R.id.b_next_cr);
        mPlaceTask = null;

        mRVAutoComplete = findViewById(R.id.rv_create_ride_autocomplete);
        mRVAutoComplete.setHasFixedSize(true);
        mRVAutoComplete.addItemDecoration(new DividerItemDecoration(mRVAutoComplete.getContext(), DividerItemDecoration.VERTICAL));
        mLayoutManagerAutoComplete = new LinearLayoutManager(this);
        mRVAutoComplete.setLayoutManager(mLayoutManagerAutoComplete);
        mPlaceData = new PlaceData[0];
        mAdapterAutoComplete = new CRAutoAdapter(CreateRideActivity.this, mPlaceData, 1);
        mRVAutoComplete.setAdapter(mAdapterAutoComplete);

        findViewById(R.id.ib_cr_from_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.rv_create_ride_autocomplete).setVisibility(View.GONE);

                String s = mETFromLocation.getText().toString().trim();

                mAdapterAutoComplete.changeType(1);

                if (mPlaceTask != null) {
                    mPlaceTask = null;
                }

                if (s.length() == 0) {
                    Toast.makeText(CreateRideActivity.this, "Field Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                s = s.replaceAll(" ", "%20");

                Log.i(LOG_CLASS, "--------------------" + s + "----------------------");
                mPlaceTask = new PlaceTask(CreateRideActivity.this);
                mPlaceTask.execute(makeURL(s));
            }
        });


        findViewById(R.id.ib_cr_to_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.rv_create_ride_autocomplete).setVisibility(View.GONE);

                String s = mETToLocation.getText().toString().trim();
                mAdapterAutoComplete.changeType(2);
                if (mPlaceTask != null) {
                    mPlaceTask = null;
                }

                if (s.length() == 0) {
                    Toast.makeText(CreateRideActivity.this, "Field Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                s = s.replaceAll(" ", "%20");

                Log.d(LOG_CLASS, "--------------------" + s + "----------------------");
                mPlaceTask = new PlaceTask(CreateRideActivity.this);
                mPlaceTask.execute(makeURL(s));
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateRideActivity.this, CreateRideOtherDetails.class);
                intent.putExtra("fromLocationDetails", fromLocationFinal);
                intent.putExtra("toLocationDetails", toLocationFinal);
                startActivity(intent);
            }
        });

    }

    private String makeURL(String str) {
        String s = URL_AUTO_PLACE_CONST
                + "input="
                + str
                + "&key="
                + getResources().getString(R.string.geo_autocomplete_api_key);
        Log.d(LOG_CLASS, "---------------->>>---------------URL is > " + s + "< ------- < ----------------");
        return s;
    }

    public void swapAutoCompleteData(PlaceData[] pt) {
        findViewById(R.id.rv_create_ride_autocomplete).setVisibility(View.VISIBLE);
        mPlaceData = null;
        if (pt != null || pt.length > 0) {
            mPlaceData = pt;
        }
        mAdapterAutoComplete.swapList(mPlaceData);
    }

    public void setFromLocation(PlaceData placeData) {
        this.fromLocationFinal = placeData;
    }

    public void setToLocation(PlaceData placeData) {
        this.toLocationFinal = placeData;
    }
}
