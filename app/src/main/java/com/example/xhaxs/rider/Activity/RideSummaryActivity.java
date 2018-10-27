package com.example.xhaxs.rider.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xhaxs.rider.Adapter.RideUserJoinSummaryAdapter;
import com.example.xhaxs.rider.Datatype.CreateRideDetailData;
import com.example.xhaxs.rider.Datatype.UserSumData;
import com.example.xhaxs.rider.LogHandle;
import com.example.xhaxs.rider.R;
import com.facebook.login.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RideSummaryActivity extends AppCompatActivity {

    private static final int JOINED_CONST = 1;
    private static final int LEFT_CONST = 2;

    private CreateRideDetailData mCreateRideDetailData;
    private CircleImageView mImageViewOwnerImage;
    private TextView mTextViewOwner;
    private TextView mTextViewToLoc;
    private TextView mTextViewFromLoc;
    private TextView mTextViewMaxRiders;
    private TextView mTextViewCurRiders;
    private TextView mRcyclerViewUsers;

    private RecyclerView mRSRecyclerView;
    private RideUserJoinSummaryAdapter mRSAdapter;
    private RecyclerView.LayoutManager mLayoutManagerJoin;

    private Button mJoinButton;
    private Button mLeaveButton;

    private ArrayList<UserSumData> userSumData;

    private FirebaseUser mCurrentUser;
    private SearchRideActivity mSearchRideActivity;
    private int rideIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_summary);

        LogHandle.checkLogin(FirebaseAuth.getInstance(), this);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        mSearchRideActivity = (SearchRideActivity) getParent();

        if (intent.hasExtra("RideDetail")) {
            mCreateRideDetailData = (CreateRideDetailData) intent.getParcelableExtra("RideDetail");
            Log.d(this.getClass().getName(), mCreateRideDetailData.toString());
        } else {
            finish();
        }

        if(intent.hasExtra("RideIndex")){
            rideIndex = intent.getIntExtra("RideIndex", -1);
        } else {
            finish();
        }

//        mImageViewOwnerImage = findViewById(R.id.iv_rsu_profile_pic);
        mTextViewToLoc = findViewById(R.id.tv_rsu_to_loc);
        mTextViewFromLoc = findViewById(R.id.tv_rsu_from_loc);
        mTextViewMaxRiders = findViewById(R.id.tv_rsu_max_coord);
        mTextViewCurRiders = findViewById(R.id.tv_rsu_cur_coord);
        mTextViewOwner = findViewById(R.id.tv_rsu_owner_name);
        mJoinButton = findViewById(R.id.bt_rsu_join_ride);
        mLeaveButton = findViewById(R.id.bt_rsu_leave_ride);

        if(mCreateRideDetailData.isOwner(mCurrentUser.getUid())){
            mJoinButton.setVisibility(View.GONE);
            mLeaveButton.setVisibility(View.GONE);
        } else {
            if(mCreateRideDetailData.isMember(mCurrentUser.getUid())){
                toggleVisibilty(JOINED_CONST);
            } else {
                toggleVisibilty(LEFT_CONST);
            }
        }

        mTextViewToLoc.setText(mCreateRideDetailData.getToLoc().toString());
        mTextViewFromLoc.setText(mCreateRideDetailData.getFromLoc().toString());
        mTextViewMaxRiders.setText(Integer.toString(mCreateRideDetailData.getMaxAccomodation()));
        mTextViewCurRiders.setText(Integer.toString(mCreateRideDetailData.getCurAccomodation()));
        mTextViewOwner.setText(mCreateRideDetailData.getRideUsers().get(0).getUname());

        /* TODO
         * 1. Add the owner Image
         * 2. Add the Owner Name
         * 3. Add From Location
         * 4. Add To Location
         * 5. Add Max Number Of Riders
         * 6. Add Current Number of Riders
         * 7. Show all the current Owners
         */

        mRSRecyclerView = findViewById(R.id.rv_rsu_rider_details);
        mRSRecyclerView.setHasFixedSize(true);
        mLayoutManagerJoin = new LinearLayoutManager(this);
        mRSRecyclerView.setLayoutManager(mLayoutManagerJoin);
        userSumData = mCreateRideDetailData.getRideUsers();
        mRSAdapter = new RideUserJoinSummaryAdapter(RideSummaryActivity.this, userSumData);
        mRSRecyclerView.setAdapter(mRSAdapter);

        mJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean updateBool = mCreateRideDetailData.addUser(
                        new UserSumData(mCurrentUser.getUid(),mCurrentUser.getDisplayName(), mCurrentUser.getEmail())
                );
                if(updateBool == true) {
                    updateDataBase("You joined the ride", JOINED_CONST);
                }
            }
        });

        mLeaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean removeBool = mCreateRideDetailData.removeUser(
                        new UserSumData(mCurrentUser.getUid(), mCurrentUser.getDisplayName(), mCurrentUser.getEmail())
                );
                if(removeBool == true){
                    updateDataBase("You left the ride", LEFT_CONST);
                }
            }
        });
    }

    private void updateDataBase(String message, final int type){
        final String fmessage = message;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> result = mCreateRideDetailData.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Riders/" + mCreateRideDetailData.getRideID(), result);
        mDatabase.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()) {
                    toggleVisibilty(type);
                    mRSAdapter.swapList(mCreateRideDetailData.getRideUsers());
                    mTextViewCurRiders.setText(Integer.toString(mCreateRideDetailData.getCurAccomodation()));
                    Toast.makeText(RideSummaryActivity.this, fmessage, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.d(this.getClass().getName(), "Error Updating Children");
                }
            }
        });
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

    private void toggleVisibilty(int type){
        if(type == JOINED_CONST){
            mJoinButton.setVisibility(View.GONE);
            mLeaveButton.setVisibility(View.VISIBLE);
        } else if(type == LEFT_CONST){
            mJoinButton.setVisibility(View.VISIBLE);
            mLeaveButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void finish(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("RideUpdate", mCreateRideDetailData);
        returnIntent.putExtra("RideIndex", rideIndex);
        setResult(RESULT_OK, returnIntent);
        super.finish();
    }
}
