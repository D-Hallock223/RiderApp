package com.example.xhaxs.rider.Activity;

import android.net.Network;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xhaxs.rider.AppUtils;
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

import org.w3c.dom.Text;

import java.util.Map;

public class ProfileViewActivity extends AppCompatActivity {

    public static final String PROFILER_STRING = "Profiler";

    private FirebaseUser mCurrentUser;
    private UserSumData toShowUser;

    private ImageView mProfilePic;
    private TextView mUserName;
    private TextView mEmail;
    private TextView mContact;
    private TextView mTotalRides;
    private TextView mGender;

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
        setContentView(R.layout.activity_profile_view);

//        mCurrentUser = LogHandle.checkLogin(FirebaseAuth.getInstance(), this);
//        LogHandle.checkDetailsAdded(mCurrentUser, this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setElevation(0);

        mCurrentUser = LogHandle.checkLogin(FirebaseAuth.getInstance(), this);
        LogHandle.checkDetailsAdded(mCurrentUser, this);

        Log.d("--------------PROFILE", mCurrentUser.getUid());

        mProfilePic = findViewById(R.id.im_apv_profile_pic);
        mUserName = findViewById(R.id.tv_apv_u_name);
        mEmail = findViewById(R.id.tv_apv_email);
        mContact = findViewById(R.id.tv_apv_contacts);
        mTotalRides = findViewById(R.id.tv_apv_total_rides);
        mGender = findViewById(R.id.tv_apv_gender);

        if(getIntent().hasExtra(PROFILER_STRING)){
            toShowUser = getIntent().getParcelableExtra(PROFILER_STRING);
        } else  {
            Toast.makeText(this, "No Riders Found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mUserName.setText(toShowUser.getUname());
        mEmail.setText(toShowUser.getEmail());
        mContact.setText("-");
        mTotalRides.setText("-");
        mGender.setText("-");

        if(!toShowUser.getUid().equals(mCurrentUser.getUid())){
            (findViewById(R.id.ll_apv_email)).setVisibility(View.GONE);
            (findViewById(R.id.ll_apv_contact)).setVisibility(View.GONE);
            loadUserDetails();
        } else {

            Map<String, Object> map = LogHandle.mapCache;

            if(map != null){
                Log.d("sdfsdfoksfd---", map.toString());
            } else {
                loadUserDetails();
                return;
            }
            String cv = "";
            String cc = "+91";
            String gender = "-";

            if(map.get("phoneNumber") != null){
                cv = map.get("phoneNumber").toString();
            }
            if(map.get("countryCode") != null){
                cc = map.get("countryCode").toString();
            }
            if(map.get("gender") != null){
                gender = ProfileDetailsActivity.AVAILABLE_GENDERS[Integer.parseInt(map.get("gender").toString())];
            }
            mContact.setText(cc + " " + cv);
            mGender.setText(gender);
        }
        loadTotalRides();

    }

    private void loadUserDetails(){
        if(AppUtils.isNetworkAvailable(this) == false){
            Toast.makeText(this, "Network Not Available", Toast.LENGTH_SHORT).show();
            mContact.setText("-");
            return;
        } else {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            db.child("Users").child(toShowUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            Log.d("-0-0-", "fetching for -> " + toShowUser.getUid());
                            if(map == null){
                                mContact.setText("-");
                                mGender.setText("-");
                                return;
                            } else {
                                Log.d("909090909", "-0-0-0-0-0-0-0-0-0-9230-4-" + map.toString());
                                String cv = "";
                                String cc = "+91";
                                String gender = "-";
                                if(map.get("phoneNumber") != null){
                                    cv = map.get("phoneNumber").toString();
                                }
                                if(map.get("countryCode") != null){
                                    cc = map.get("countryCode").toString();
                                }
                                if(map.get("gender") != null){
                                    gender = ProfileDetailsActivity.AVAILABLE_GENDERS[Integer.parseInt(map.get("gender").toString())];
                                }
                                mContact.setText(cc + " " + cv);
                                mGender.setText(gender);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void loadTotalRides(){
        if(AppUtils.isNetworkAvailable(this) == false){
            Toast.makeText(this, "Network Not Available", Toast.LENGTH_SHORT).show();
            mTotalRides.setText("-");
            return;
        } else {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            db.child("Riders").orderByChild(CreateRideDetailData.RIDE_USER_ARRAY_STRING + "/"
                    + toShowUser.getUid() + "/"
                    + UserSumData.UID_STRING)
                    .equalTo(toShowUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            if(map == null){
                                mTotalRides.setText("0");
                                return;
                            } else {
                                mTotalRides.setText(Integer.toString(map.size()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
