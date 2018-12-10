package com.example.xhaxs.rider.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xhaxs.rider.Adapter.RideUserJoinSummaryAdapter;
import com.example.xhaxs.rider.AppUtils;
import com.example.xhaxs.rider.Datatype.CreateRideDetailData;
import com.example.xhaxs.rider.Datatype.UserSumData;
import com.example.xhaxs.rider.LogHandle;
import com.example.xhaxs.rider.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.squareup.picasso.MemoryPolicy;
//import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RideSummaryActivity extends AppCompatActivity {

    public static final int JOINED_CONST = 1;
    public static final int LEFT_CONST = 2;
    public static final int FINISHED_RIDE_CONST = 3;
    public static final int FINISH_BY_OWNER = 4;
    public static final int NOTHING = -1;
    private CreateRideDetailData mCreateRideDetailData;
    private CircleImageView mImageViewOwnerImage;
    private TextView mTextViewOwner;
    private TextView mTextViewToLoc;
    private TextView mTextViewFromLoc;
    private TextView mTextViewMaxRiders;
    private TextView mTextViewCurRiders;
    private TextView mRcyclerViewUsers;
    private TextView mTextViewRideFinishMessage;
    private TextView mTextViewStartTime;
    private TextView mTextViewRideCancelMessage;

    private RecyclerView mRSRecyclerView;
    private RideUserJoinSummaryAdapter mRSAdapter;
    private RecyclerView.LayoutManager mLayoutManagerJoin;

    private Button mJoinButton;
    private Button mLeaveButton;
    private Button mFinishRideButton;
    private Button mCancelRideButton;

    private LinearLayout mButtonGetStartDirections;
    private LinearLayout mLLLeaveButton;

    private LinearLayout mGetDirectionsButton;
    private LinearLayout mStartRideButtons;

    private ArrayList<UserSumData> userSumData;

    private FirebaseUser mCurrentUser;
    private SearchRideActivity mSearchRideActivity;
    private int rideIndex;

    private int RESULT_CODE;

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

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_ride_summary);

        mCurrentUser = LogHandle.checkLogin(FirebaseAuth.getInstance(), this);
        LogHandle.checkDetailsAdded(mCurrentUser, this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RESULT_CODE = NOTHING;

        Intent intent = getIntent();
        mSearchRideActivity = (SearchRideActivity) getParent();

        if (intent.hasExtra("RideDetail")) {
            mCreateRideDetailData = (CreateRideDetailData) intent.getParcelableExtra("RideDetail");
            Log.d(this.getClass().getName(), mCreateRideDetailData.toString());
        } else {
            finish();
        }

        if (intent.hasExtra("RideIndex")) {
            rideIndex = intent.getIntExtra("RideIndex", -1);
        } else {
            finish();
        }

        mImageViewOwnerImage = findViewById(R.id.iv_rsu_profile_pic);
        mTextViewToLoc = findViewById(R.id.tv_rsu_to_loc);
        mTextViewFromLoc = findViewById(R.id.tv_rsu_from_loc);
        mTextViewMaxRiders = findViewById(R.id.tv_rsu_max_coord);
        mTextViewCurRiders = findViewById(R.id.tv_rsu_cur_coord);
        mTextViewOwner = findViewById(R.id.tv_rsu_owner_name);
        mJoinButton = findViewById(R.id.bt_rsu_join_ride);
        mLeaveButton = findViewById(R.id.bt_rsu_leave_ride);
        mGetDirectionsButton = findViewById(R.id.bt_ll_rsu_get_dir);
        mFinishRideButton = findViewById(R.id.bt_rsu_finish_ride);
        mStartRideButtons = findViewById(R.id.ll_rsu_on_ride_start);
        mTextViewRideFinishMessage = findViewById(R.id.tv_rsu_finished_ride_message);
        mTextViewStartTime = findViewById(R.id.tv_rsu_journey_time);
        mButtonGetStartDirections = findViewById(R.id.bt_ll_rsu_start_dir);
        mLLLeaveButton = findViewById(R.id.ll_leave_button_layout);
        mCancelRideButton = findViewById(R.id.bt_rsu_cancel_ride);
        mTextViewRideCancelMessage = findViewById(R.id.tv_rsu_cancelled_ride_message);


        mStartRideButtons.setVisibility(View.GONE);
        mTextViewRideFinishMessage.setVisibility(View.GONE);
        mCancelRideButton.setVisibility(View.GONE);
        mTextViewRideCancelMessage.setVisibility(View.GONE);

        if (mCreateRideDetailData.isOwner(mCurrentUser.getUid())) {
            mJoinButton.setVisibility(View.GONE);
            mLLLeaveButton.setVisibility(View.GONE);
            checkIfTimeToStartRide();
            rideFinished();
        } else {

            mCancelRideButton.setVisibility(View.GONE);
            mTextViewRideCancelMessage.setVisibility(View.GONE);

            if (rideFinished() == false) {
                if (mCreateRideDetailData.isMember(mCurrentUser.getUid())) {
                    toggleVisibilty(JOINED_CONST);
                } else {
                    toggleVisibilty(LEFT_CONST);
                }
            }
        }

            FirebaseDatabase.getInstance().getReference()
                    .child("Users/" + mCreateRideDetailData.getRideOwner().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            if(map == null) return;
                            else {
                                if(map.get(AppUtils.PROFILE_PIC_URL_STRING) != null && map.get(AppUtils.PROFILE_PIC_SIZE_STRING) != null) {
                                    Log.d("----sdflksnfdlkndfl", "\n\n\n" + "\t----Loading data----\n\n\n");
//                                    Picasso.get().load(map.get(AppUtils.PROFILE_PIC_URL_STRING).toString()).memoryPolicy(MemoryPolicy.NO_CACHE).into(mImageViewOwnerImage);
//                                    AppUtils.loadImage(map.get(AppUtils.PROFILE_PIC_URL_STRING).toString(), mImageViewOwnerImage);
                                    AppUtils.loadFromNetImage(RideSummaryActivity.this,
                                            map.get(AppUtils.PROFILE_PIC_URL_STRING).toString(),
                                            mImageViewOwnerImage,
                                            Integer.parseInt(map.get(AppUtils.PROFILE_PIC_SIZE_STRING).toString()));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
//        }

        mTextViewToLoc.setText("To: " + mCreateRideDetailData.getToLoc().toString());
        mTextViewFromLoc.setText("From: " + mCreateRideDetailData.getFromLoc().toString());
        mTextViewMaxRiders.setText(Integer.toString(mCreateRideDetailData.getMaxAccomodation()));
        mTextViewCurRiders.setText(Integer.toString(mCreateRideDetailData.getCurAccomodation()));
        mTextViewOwner.setText(mCreateRideDetailData.getRideUsers().get(0).getUname());

        java.text.DateFormat dateFormat = java.text.DateFormat
                .getDateTimeInstance(java.text.DateFormat.SHORT, java.text.DateFormat.SHORT);
        String strStartTime = dateFormat.format(mCreateRideDetailData.getJourneyTime().getTime());
        mTextViewStartTime.setText("Starts On: " + strStartTime);

        mRSRecyclerView = findViewById(R.id.rv_rsu_rider_details);
        mRSRecyclerView.setHasFixedSize(true);
        mLayoutManagerJoin = new LinearLayoutManager(this);
        mRSRecyclerView.setLayoutManager(mLayoutManagerJoin);
        userSumData = mCreateRideDetailData.getRideUsers();
        mRSAdapter = new RideUserJoinSummaryAdapter(RideSummaryActivity.this,
                userSumData,
                mCreateRideDetailData.isOwner(mCurrentUser.getUid()),
                mCreateRideDetailData.getRideOwner().getUid(),
                (mCreateRideDetailData.getRideFinished() == CreateRideDetailData.RIDE_FINSISHED) ||
                        (mCreateRideDetailData.getRideFinished() == CreateRideDetailData.RIDE_CANCELLED)
        );

        mRSRecyclerView.setAdapter(mRSAdapter);

        mJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean updateBool = mCreateRideDetailData.addUser(
                        new UserSumData(mCurrentUser.getUid(), mCurrentUser.getDisplayName(), mCurrentUser.getEmail())
                );
                if (updateBool == true) {
                    updateDataBase("You joined the ride", JOINED_CONST);
                }
            }
        });

        mLeaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(RideSummaryActivity.this)
                        .setTitle("Leave Ride")
                        .setMessage("Do you really want to leave this ride?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean removeBool = mCreateRideDetailData.removeUser(
                                        new UserSumData(mCurrentUser.getUid(), mCurrentUser.getDisplayName(), mCurrentUser.getEmail())
                                );
                                if (removeBool == true) {
                                    updateDataBase("You left the ride", LEFT_CONST);
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        mGetDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLng = mCreateRideDetailData.getToLoc().getLatLng();
                Uri destinationIntentUri = Uri.parse("google.navigation:q="
                        + Double.toString(latLng.latitude)
                        + ","
                        + Double.toString(latLng.longitude));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, destinationIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(RideSummaryActivity.this, "Your Phone does not support GPS navigation", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mFinishRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(RideSummaryActivity.this)
                        .setTitle("Finish Ride")
                        .setMessage("Do you really want to Finish this ride?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mCreateRideDetailData.setRideFinished(CreateRideDetailData.RIDE_FINSISHED)) {
                                    updateDataBase("Ride Finshed", FINISHED_RIDE_CONST);
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        mCancelRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(RideSummaryActivity.this)
                        .setTitle("Cancel Ride")
                        .setMessage("Do you really want to Cancel this ride?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mCreateRideDetailData.setRideFinished(CreateRideDetailData.RIDE_CANCELLED)) {
                                    updateDataBase("Ride Finshed", FINISHED_RIDE_CONST);
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });


        mButtonGetStartDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLng = mCreateRideDetailData.getFromLoc().getLatLng();
                Uri destinationIntentUri = Uri.parse("google.navigation:q="
                        + Double.toString(latLng.latitude)
                        + ","
                        + Double.toString(latLng.longitude));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, destinationIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(RideSummaryActivity.this, "Your Phone does not support GPS navigation", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateDataBase(String message, final int type) {
        final String fmessage = message;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> result = mCreateRideDetailData.toMap();

        Log.d("--->>--->>---", result.toString());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Riders/" + mCreateRideDetailData.getRideID(), result);
        mDatabase.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    if (type != FINISHED_RIDE_CONST) {
                        toggleVisibilty(type);
                        mRSAdapter.swapList(mCreateRideDetailData.getRideUsers());
                        mTextViewCurRiders.setText(Integer.toString(mCreateRideDetailData.getCurAccomodation()));
                    }
                    Toast.makeText(RideSummaryActivity.this, fmessage, Toast.LENGTH_SHORT).show();
                    RESULT_CODE = type;
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
            case R.id.menu_my_profile:
                Intent intent = new Intent(this, ProfileViewActivity.class);
                intent.putExtra(ProfileViewActivity.PROFILER_STRING,
                        new UserSumData(mCurrentUser.getUid(), mCurrentUser.getDisplayName(), mCurrentUser.getEmail()));
                startActivity(intent);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }
    }

    private void toggleVisibilty(int type) {
        if (type == JOINED_CONST) {
            mJoinButton.setVisibility(View.GONE);
            mLLLeaveButton.setVisibility(View.VISIBLE);
        } else if (type == LEFT_CONST) {
            mJoinButton.setVisibility(View.VISIBLE);
            mLLLeaveButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void finish() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("RideUpdate", mCreateRideDetailData);
        returnIntent.putExtra("RideIndex", rideIndex);
        returnIntent.putExtra("ResultCode", RESULT_CODE);
        setResult(RESULT_OK, returnIntent);
        super.finish();
    }

    private void checkIfTimeToStartRide() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.getTimeInMillis() > mCreateRideDetailData.getJourneyTime().getTimeInMillis() && mCreateRideDetailData.getRideFinished() != CreateRideDetailData.RIDE_FINSISHED) {
            if(checkCancelRide(true) == false) {
                mStartRideButtons.setVisibility(View.VISIBLE);
            }
        } else {
            checkCancelRide(false);
            mStartRideButtons.setVisibility(View.GONE);
        }
    }

    private boolean checkCancelRide(boolean timeUp){
        if(mCreateRideDetailData.getRideFinished() == CreateRideDetailData.RIDE_CANCELLED){
            mCancelRideButton.setVisibility(View.GONE);
            mTextViewRideCancelMessage.setVisibility(View.VISIBLE);
            return true;
        } else {
            mTextViewRideCancelMessage.setVisibility(View.GONE);
            if(timeUp == false){
                mCancelRideButton.setVisibility(View.VISIBLE);
            } else {
                mCancelRideButton.setVisibility(View.GONE);
            }
        }
        return false;
    }

    private boolean rideFinished() {
        if ((mCreateRideDetailData.getRideFinished() == CreateRideDetailData.RIDE_FINSISHED)) {
            mCancelRideButton.setVisibility(View.GONE);
            mTextViewRideCancelMessage.setVisibility(View.GONE);
            mTextViewRideCancelMessage.setVisibility(View.GONE);
            mTextViewRideFinishMessage.setVisibility(View.VISIBLE);
            mJoinButton.setVisibility(View.GONE);
            mLLLeaveButton.setVisibility(View.GONE);
            return true;
        } else if((mCreateRideDetailData.getRideFinished() == CreateRideDetailData.RIDE_CANCELLED)) {
            mCancelRideButton.setVisibility(View.GONE);
            mTextViewRideFinishMessage.setVisibility(View.GONE);
            mTextViewRideCancelMessage.setVisibility(View.VISIBLE);
            mJoinButton.setVisibility(View.GONE);
            mLLLeaveButton.setVisibility(View.GONE);
            return true;
        }
        else {
            mTextViewRideFinishMessage.setVisibility(View.GONE);
            mTextViewRideCancelMessage.setVisibility(View.GONE);
            return false;
        }
    }

    public void removeUserFromList(final UserSumData userSumData) {

        new AlertDialog.Builder(this)
                .setTitle("Remove User")
                .setMessage("Do you want to remove " + userSumData.getUname() + "?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean removeBool = mCreateRideDetailData.removeUser(
                                userSumData.getUid()
                        );
                        if (removeBool == true) {
                            updateDataBase("User Removed!", FINISH_BY_OWNER);
                        }
                    }
                }).setNegativeButton(android.R.string.no, null).show();
    }
}
