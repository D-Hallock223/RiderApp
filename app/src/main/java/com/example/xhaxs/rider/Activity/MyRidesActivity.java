package com.example.xhaxs.rider.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.xhaxs.rider.Adapter.MyRidesAdapter;
import com.example.xhaxs.rider.CommonBottomNavigationHandle;
import com.example.xhaxs.rider.Datatype.CreateRideDetailData;
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

    private final int REQUEST_CODE = 101;

    private BottomNavigationView mBottomNavigationView;
    private RecyclerView mRVMyRides;
    private MyRidesAdapter mMyRidesAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<CreateRideDetailData> mCreateRideDetailData;

    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rides);

        mCurrentUser = LogHandle.checkLogin(FirebaseAuth.getInstance(), MyRidesActivity.this);

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

        Log.d("----", "--Called Loader Manager My Rides Activity");
        loadMyData();
    }

    private void loadMyData(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Riders")
                .orderByChild("ride_users/" + mCurrentUser.getUid() + "/uid").equalTo(mCurrentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                        Log.d("MYRIDES----<<<<<<","Recieved Following Data For User --> " + mCurrentUser.getUid());
                        if(map == null) {
                            return;
                        }
                        Log.d("-=-=-=-=-=", map.toString());
                        ArrayList<CreateRideDetailData> createRideDetailDataArrayList = new ArrayList<>();

                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            Map<String, Object> ride = (Map<String, Object>) entry.getValue();
                            Log.d("------------", "Printing ride value -- \n" +
                                    ride.toString()
                            );

                            createRideDetailDataArrayList.add(new CreateRideDetailData(entry.getKey(), (Map<String, Object>)entry.getValue()));
                        }
                        Log.d("----<<<<<<","--------------->>>>>>>>>>....");
                        swapPosData(createRideDetailDataArrayList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void showDetails(CreateRideDetailData createRideDetailData, int index) {
        Toast.makeText(MyRidesActivity.this, "Called Show Details", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, RideSummaryActivity.class);
        intent.putExtra("RideDetail", createRideDetailData);
        intent.putExtra("RideIndex",  index);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void swapPosData(ArrayList<CreateRideDetailData> createRideDetailData) {
        mCreateRideDetailData = null;
        if (createRideDetailData != null) {
            mCreateRideDetailData = createRideDetailData;
        }
        mMyRidesAdapter.swapList(mCreateRideDetailData);
    }

    public void updateAdapterAtIndex(CreateRideDetailData createRideDetailData, int index){
        if(!createRideDetailData.isMember(mCurrentUser.getUid())) {
            mCreateRideDetailData.remove(index);
            mMyRidesAdapter.swapList(mCreateRideDetailData);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            final CreateRideDetailData createRideDetailData = data.getParcelableExtra("RideUpdate");
            final int rideIndex = data.getIntExtra("RideIndex", -1);
            if(rideIndex != -1){
                new Runnable(){
                    @Override
                    public void run() {
                        updateAdapterAtIndex(createRideDetailData, rideIndex);
                    }
                }.run();
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

            default:
                return false;
        }
    }
}
