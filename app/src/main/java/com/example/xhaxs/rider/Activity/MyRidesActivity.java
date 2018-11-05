package com.example.xhaxs.rider.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xhaxs.rider.Adapter.MyRidesAdapter;
import com.example.xhaxs.rider.AppUtils;
import com.example.xhaxs.rider.CommonBottomNavigationHandle;
import com.example.xhaxs.rider.Datatype.CreateRideDetailData;
import com.example.xhaxs.rider.Datatype.UserSumData;
import com.example.xhaxs.rider.LogHandle;
import com.example.xhaxs.rider.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MyRidesActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 101;
    private static final String NO_DATA_FOUND = "No Rides Found";
    private static final String FETCHING_DATA = "Fetching Rides...";
    private static final String ERROR_FETCHING = "Error Fetching Data!";
    ArrayList<CreateRideDetailData> mCreateRideDetailData;
    private TextView mMessage;
    private LinearLayout mNetworkErrorLayout;
    private Button mNetTryAgainButton;
    private BottomNavigationView mBottomNavigationView;
    private RecyclerView mRVMyRides;
    private MyRidesAdapter mMyRidesAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onStart() {
        super.onStart();
        mCurrentUser = LogHandle.checkLogin(FirebaseAuth.getInstance(), this);
        LogHandle.checkDetailsAdded(mCurrentUser, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentUser = LogHandle.checkLogin(FirebaseAuth.getInstance(), this);
        LogHandle.checkDetailsAdded(mCurrentUser, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rides);

        getSupportActionBar().setTitle("My Rides");

//        mCurrentUser = LogHandle.checkLogin(FirebaseAuth.getInstance(), MyRidesActivity.this);
//        LogHandle.checkDetailsAdded(mCurrentUser, this);

        mMessage = findViewById(R.id.tv_my_rides_messages);

        mNetworkErrorLayout = findViewById(R.id.ll_network_unavailable);
        mNetTryAgainButton = findViewById(R.id.bt_net_try_again);

        mMessage.setText(FETCHING_DATA);


        mCreateRideDetailData = null;

        mBottomNavigationView = findViewById(R.id.bn_bottom_nav);
        mBottomNavigationView.setSelectedItemId(R.id.mi_my_ride);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                CommonBottomNavigationHandle.chooseSelectedActivity(MyRidesActivity.this, menuItem);
                return true;
            }
        });

        mRVMyRides = findViewById(R.id.rv_my_rides_cont);
        mRVMyRides.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRVMyRides.setLayoutManager(mLayoutManager);
        mCreateRideDetailData = new ArrayList<>();
        mMyRidesAdapter = new MyRidesAdapter(MyRidesActivity.this, mCreateRideDetailData);
        mRVMyRides.setAdapter(mMyRidesAdapter);

        if (mCreateRideDetailData == null) {
            mNetworkErrorLayout.setVisibility(View.GONE);
            mRVMyRides.setVisibility(View.GONE);
            mMessage.setVisibility(View.VISIBLE);
            mMessage.setText(FETCHING_DATA);
        }

        mNetTryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMyData();
            }
        });

        loadMyData();
    }

    private void loadMyData() {

        mRVMyRides.setVisibility(View.GONE);
        mNetworkErrorLayout.setVisibility(View.GONE);
        mMessage.setVisibility(View.VISIBLE);
        mMessage.setText(FETCHING_DATA);

        if (AppUtils.isNetworkAvailable(this) == false) {
            mMessage.setVisibility(View.GONE);
//            mMessage.setText(AppUtils.NETWORK_ERROR);
            mNetworkErrorLayout.setVisibility(View.VISIBLE);
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Riders")
                .orderByChild("ride_users/" + mCurrentUser.getUid() + "/uid").equalTo(mCurrentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                        Log.d("MYRIDES----<<<<<<", "Recieved Following Data For User --> " + mCurrentUser.getUid());
                        if (map == null) {
                            mMessage.setVisibility(View.VISIBLE);
                            mMessage.setText(NO_DATA_FOUND);
                            return;
                        }
                        Log.d("-=-=-=-=-=", map.toString());
                        ArrayList<CreateRideDetailData> createRideDetailDataArrayList = new ArrayList<>();

                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            Map<String, Object> ride = (Map<String, Object>) entry.getValue();
                            Log.d("------------", "Printing ride value -- \n" +
                                    ride.toString()
                            );

                            createRideDetailDataArrayList.add(new CreateRideDetailData(entry.getKey(), (Map<String, Object>) entry.getValue()));
                        }
                        Log.d("----<<<<<<", "--------------->>>>>>>>>>....");

                        if (createRideDetailDataArrayList.size() == 0) {
                            mRVMyRides.setVisibility(View.GONE);
                            mMessage.setVisibility(View.VISIBLE);
                            mMessage.setText(NO_DATA_FOUND);
                        } else {
                            mMessage.setVisibility(View.GONE);
                            mRVMyRides.setVisibility(View.VISIBLE);
                        }
                        swapPosData(createRideDetailDataArrayList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        mMessage.setVisibility(View.VISIBLE);
                        mMessage.setText(ERROR_FETCHING);
                    }
                });
    }

    public void showDetails(CreateRideDetailData createRideDetailData, int index) {
        //  Toast.makeText(MyRidesActivity.this, "Called Show Details", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, RideSummaryActivity.class);
        intent.putExtra("RideDetail", createRideDetailData);
        intent.putExtra("RideIndex", index);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void swapPosData(ArrayList<CreateRideDetailData> createRideDetailData) {
        mCreateRideDetailData = null;
        if (createRideDetailData != null) {
            mCreateRideDetailData = createRideDetailData;
        }
        mMyRidesAdapter.swapList(mCreateRideDetailData);
    }

    public void updateAdapterAtIndex(CreateRideDetailData createRideDetailData, int index, int resultIndex) {
        if (resultIndex == RideSummaryActivity.LEFT_CONST && !createRideDetailData.isMember(mCurrentUser.getUid())) {
            mCreateRideDetailData.remove(index);
            mMyRidesAdapter.swapList(mCreateRideDetailData);
        } else if (resultIndex == RideSummaryActivity.FINISH_BY_OWNER || resultIndex == RideSummaryActivity.FINISHED_RIDE_CONST) {
            mCreateRideDetailData.set(index, createRideDetailData);
            mMyRidesAdapter.swapList(mCreateRideDetailData);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            final CreateRideDetailData createRideDetailData = data.getParcelableExtra("RideUpdate");
            final int rideIndex = data.getIntExtra("RideIndex", -1);
            final int resultIndex = data.getIntExtra("ResultCode", RideSummaryActivity.NOTHING);
            if (rideIndex != -1) {
                updateAdapterAtIndex(createRideDetailData, rideIndex, resultIndex);
            }
        }
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
