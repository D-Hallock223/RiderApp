package com.example.xhaxs.rider.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.xhaxs.rider.Datatype.CreateRideDetailData;
import com.example.xhaxs.rider.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class RideSummaryActivity extends AppCompatActivity {

    private CreateRideDetailData mCreateRideDetailData;
    private CircleImageView mImageViewOwnerImage;
    private TextView mTextViewToLoc;
    private TextView mTextViewFromLoc;
    private TextView mTextViewMaxRiders;
    private TextView mTextViewCurRiders;
    private TextView mRcyclerViewUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_summary);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if (intent.hasExtra("RideDetail")) {
            mCreateRideDetailData = (CreateRideDetailData) intent.getSerializableExtra("RideDetail");
            Log.d(this.getClass().getName(), mCreateRideDetailData.toString());
        } else {
            finish();
        }

//        mImageViewOwnerImage = findViewById(R.id.iv_rsu_profile_pic);
        mTextViewToLoc = findViewById(R.id.tv_rsu_to_loc);
        mTextViewFromLoc = findViewById(R.id.tv_rsu_from_loc);
        mTextViewMaxRiders = findViewById(R.id.tv_rsu_max_coord);
        mTextViewCurRiders = findViewById(R.id.tv_rsu_cur_coord);

        mTextViewToLoc.setText(mCreateRideDetailData.getToLoc().toString());
        mTextViewFromLoc.setText(mCreateRideDetailData.getFromLoc().toString());
        mTextViewMaxRiders.setText(Integer.toString(mCreateRideDetailData.getMaxAccomodation()));
        mTextViewCurRiders.setText(Integer.toString(mCreateRideDetailData.getCurAccomodation()));

        /* TODO
         * 1. Add the owner Image
         * 2. Add the Owner Name
         * 3. Add From Location
         * 4. Add To Location
         * 5. Add Max Number Of Riders
         * 6. Add Current Number of Riders
         * 7. Show all the current Owners
         */
    }
}
