package com.example.xhaxs.rider.AsyncTasks;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.example.xhaxs.rider.Activity.SearchRideActivity;
import com.example.xhaxs.rider.AppUtils;
import com.example.xhaxs.rider.Datatype.CreateRideDetailData;
import com.example.xhaxs.rider.Datatype.PlaceData;
import com.example.xhaxs.rider.Datatype.UserSumData;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class RideDetailTask extends AsyncTask<String, Void, CreateRideDetailData[]> {

    private SearchRideActivity mSearchRideActivity;
    private ArrayList<CreateRideDetailData> mCreateRideDetailData;
    private Map<String, Object> mapFrom;

    private GeoDataClient mGeoDataClient;

    public RideDetailTask(SearchRideActivity searchRideActivity) {
        mSearchRideActivity = searchRideActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mSearchRideActivity.setRV(View.GONE);
        mSearchRideActivity.mMessageTextView.setVisibility(View.VISIBLE);
        mSearchRideActivity.mMessageTextView.setText(SearchRideActivity.FETCHING_RIDES);
    }

    @Override
    protected CreateRideDetailData[] doInBackground(String... strings) {
        Log.d(this.getClass().getName(), "------Getting Possible Rides----------" + strings[0]);
        return new CreateRideDetailData[0];
    }

    @Override
    protected void onPostExecute(CreateRideDetailData[] createRideDetailData) {

        if(AppUtils.isNetworkAvailable(mSearchRideActivity.getApplicationContext()) == false){
            mSearchRideActivity.setRV(View.GONE);
            mSearchRideActivity.mMessageTextView.setVisibility(View.VISIBLE);
            mSearchRideActivity.mMessageTextView.setText(AppUtils.NETWORK_ERROR);
            return;
        }

        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Riders")
                .orderByChild("from_loc/id").equalTo(mSearchRideActivity.getFromValue().getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> mapFrom = (Map<String, Object>) dataSnapshot.getValue();
                        Log.d(this.getClass().getName(), "___________________________________________");
                        Log.d(this.getClass().getName(), "_----------------------Searching for " + mSearchRideActivity.getFromValue().getId());

                        if(mapFrom == null){
                            Log.d(this.getClass().getName(), "______________NOTHING FOUND_____________");
                            mSearchRideActivity.swapPosData(new ArrayList<CreateRideDetailData>());
                            mSearchRideActivity.mMessageTextView.setVisibility(View.VISIBLE);
                            mSearchRideActivity.mMessageTextView.setText(SearchRideActivity.NO_DATA_FOUND);
                            return;
                        }

                        Log.d(this.getClass().getName(), mapFrom.toString());
                        Log.d(this.getClass().getName(), "___________________________________________");

                        ArrayList<CreateRideDetailData> createRideDetailDataArrayList = new ArrayList<>();

                        for (Map.Entry<String, Object> entry : mapFrom.entrySet()) {
                            Map<String, Object> ride = (Map<String, Object>) entry.getValue();
                            Log.d("------------", "Printing ride value -- \n" +
                                    ride.toString()
                                );
                            if (((Map<String, Object>)ride.get(CreateRideDetailData.TO_LOC_STRING))
                                    .get("id")
                                    .toString()
                                    .contentEquals(mSearchRideActivity.getTovalue().getId())
                                    &&
                                  (Integer.parseInt(ride.get(CreateRideDetailData.RIDE_FINISH_STRING).toString()) == CreateRideDetailData.RIDE_UNDONE)
                                ) {

                                Log.d(this.getClass().getName(), "************** CALLING MAP FROM RIDE DETAIL TASK *************************");
                                createRideDetailDataArrayList.add(new CreateRideDetailData(entry.getKey(), (Map<String, Object>)entry.getValue()));
                            }
                        }

                        mCreateRideDetailData = createRideDetailDataArrayList;
//                        for(int i = 0; i < createRideDetailDataArrayList.size(); ++i){
//                            mCreateRideDetailData[i] = createRideDetailDataArrayList.get(i);
//                        }

                        if(mCreateRideDetailData != null && mCreateRideDetailData.size() > 0) {
                            mSearchRideActivity.mMessageTextView.setVisibility(View.GONE);
                            mSearchRideActivity.setRV(View.VISIBLE);
                            mSearchRideActivity.swapPosData(mCreateRideDetailData);
                        } else {
                            mSearchRideActivity.mMessageTextView.setVisibility(View.VISIBLE);
                            mSearchRideActivity.mMessageTextView.setText(SearchRideActivity.NO_DATA_FOUND);
                            mSearchRideActivity.swapPosData(new ArrayList<CreateRideDetailData>(0));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}