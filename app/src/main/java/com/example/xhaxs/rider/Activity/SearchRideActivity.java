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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.xhaxs.rider.Adapter.SRAutoAdapter;
import com.example.xhaxs.rider.Adapter.SRPosAdapter;
import com.example.xhaxs.rider.AsyncTasks.PlaceTaskSR;
import com.example.xhaxs.rider.AsyncTasks.RideDetailTask;
import com.example.xhaxs.rider.Datatype.CreateRideDetailData;
import com.example.xhaxs.rider.Datatype.PlaceData;
import com.example.xhaxs.rider.R;

public class SearchRideActivity extends AppCompatActivity {

    private static final String LOG_CLASS = CreateRideActivity.class.getName();
    private static final String URL_AUTO_PLACE_CONST = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";
    private final Activity activity = this;
    private EditText mETFromLocation;
    private EditText mETToLocation;
    private ImageButton mFabCreateNewRide;
    private RecyclerView mRVAutoComplete;
    private SRAutoAdapter mAdapterAutoComplete;
    private RecyclerView.LayoutManager mLayoutManagerAutoComplete;
    private RecyclerView mRVPossibleLocations;
    private SRPosAdapter mSRPosAdapter;
    private RecyclerView.LayoutManager mLayoutManagerPos;
    private ImageButton mSearchAvailRides;
    private PlaceTaskSR mPlaceTask;
    private PlaceData[] mPlaceData;
    private RideDetailTask mRideDetailTask;
    private CreateRideDetailData[] mCreateRideDetailData;
    private PlaceData fromLocationFinal;
    private PlaceData toLocationFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ride);

        getSupportActionBar().setElevation(0);

        mFabCreateNewRide = findViewById(R.id.fab_create_new_ride_sr);

        mFabCreateNewRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchRideActivity.this, CreateRideActivity.class);
                startActivity(intent);
            }
        });

        mETFromLocation = findViewById(R.id.et_sr_from_location);
        mETToLocation = findViewById(R.id.et_sr_to_location);
        mSearchAvailRides = findViewById(R.id.ib_search_src_dest_sr);
        mPlaceTask = null;
        mRideDetailTask = null;

        mRVAutoComplete = findViewById(R.id.rv_sr_autocomplete);
        mRVAutoComplete.setHasFixedSize(true);
        mRVAutoComplete.addItemDecoration(new DividerItemDecoration(mRVAutoComplete.getContext(), DividerItemDecoration.VERTICAL));
        mLayoutManagerAutoComplete = new LinearLayoutManager(this);
        mRVAutoComplete.setLayoutManager(mLayoutManagerAutoComplete);
        mPlaceData = new PlaceData[0];
        mAdapterAutoComplete = new SRAutoAdapter(SearchRideActivity.this, mPlaceData, 1);
        mRVAutoComplete.setAdapter(mAdapterAutoComplete);

        mRVPossibleLocations = findViewById(R.id.rv_sr_locations_possible);
        mRVPossibleLocations.setHasFixedSize(true);
        mLayoutManagerPos = new LinearLayoutManager(this);
        mRVPossibleLocations.setLayoutManager(mLayoutManagerPos);
        mCreateRideDetailData = new CreateRideDetailData[0];
        mSRPosAdapter = new SRPosAdapter(SearchRideActivity.this, mCreateRideDetailData);
        mRVPossibleLocations.setAdapter(mSRPosAdapter);


        findViewById(R.id.ib_sr_from_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFromLocation(null);

                findViewById(R.id.rv_sr_autocomplete).setVisibility(View.GONE);

                String s = mETFromLocation.getText().toString().trim();

                mAdapterAutoComplete.changeType(1);

                if (mPlaceTask != null) {
                    mPlaceTask = null;
                }

                if (s.length() == 0) {
                    Toast.makeText(SearchRideActivity.this, "Field Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                s = s.replaceAll(" ", "%20");

                Log.i(LOG_CLASS, "--------------------" + s + "----------------------");
                mPlaceTask = new PlaceTaskSR(SearchRideActivity.this);
                mPlaceTask.execute(makeURL(s));
            }
        });


        findViewById(R.id.ib_sr_to_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToLocation(null);

                findViewById(R.id.rv_sr_autocomplete).setVisibility(View.GONE);

                String s = mETToLocation.getText().toString().trim();
                mAdapterAutoComplete.changeType(2);
                if (mPlaceTask != null) {
                    mPlaceTask = null;
                }

                if (s.length() == 0) {
                    Toast.makeText(SearchRideActivity.this, "Field Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                s = s.replaceAll(" ", "%20");

                Log.d(LOG_CLASS, "--------------------" + s + "----------------------");
                mPlaceTask = new PlaceTaskSR(SearchRideActivity.this);
                mPlaceTask.execute(makeURL(s));
            }
        });

        mSearchAvailRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(LOG_CLASS, "------retrieving possible Location result--------");
                if (mRideDetailTask != null) {
                    mRideDetailTask.cancel(true);
                    mRideDetailTask = null;
                }

                if (fromLocationFinal == null || toLocationFinal == null) return;
                Log.d(LOG_CLASS, "------retrieving possible Location result: Strings Available--------");
                mRVAutoComplete.setVisibility(View.GONE);
                mRVPossibleLocations.setVisibility(View.GONE);
                Log.d(LOG_CLASS, "------retrieving possible Location result: View visibility none--------");
                mRideDetailTask = new RideDetailTask(SearchRideActivity.this);
                mRideDetailTask.execute(makePosRideURL());
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
        findViewById(R.id.rv_sr_autocomplete).setVisibility(View.VISIBLE);
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

    public void showDetails(CreateRideDetailData createRideDetailData) {
        Intent intent = new Intent(this, RideSummaryActivity.class);
        intent.putExtra("RideDetail", createRideDetailData);
        startActivity(intent);
    }

    public void swapPosData(CreateRideDetailData[] createRideDetailData) {
        mRVPossibleLocations.setVisibility(View.VISIBLE);
        mCreateRideDetailData = null;
        if (createRideDetailData != null) {
            mCreateRideDetailData = createRideDetailData;
        }
        mSRPosAdapter.swapList(mCreateRideDetailData);
    }

    public String makePosRideURL() {
        return "temporary URL -> " + fromLocationFinal.toString() + " - " + toLocationFinal.toString();
    }
}
