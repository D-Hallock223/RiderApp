package com.example.xhaxs.rider.AsyncTasks;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.xhaxs.rider.Activity.SearchRideActivity;
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
    private CreateRideDetailData[] mCreateRideDetailData;
    private Map<String, Object> mapFrom;

    private GeoDataClient mGeoDataClient;

    public RideDetailTask(SearchRideActivity searchRideActivity) {
        mSearchRideActivity = searchRideActivity;
    }

    private CreateRideDetailData[] tempo(PlaceData to, PlaceData from) {
        UserSumData userSumData = new UserSumData("1", "temp", "temp@gmail.com");
        mCreateRideDetailData = new CreateRideDetailData[5];
        mCreateRideDetailData[0] = new CreateRideDetailData(userSumData, from,
                to,
                Calendar.getInstance(),
                4);
        mCreateRideDetailData[1] = new CreateRideDetailData(userSumData, from,
                to,
                Calendar.getInstance(),
                4);
        mCreateRideDetailData[2] = new CreateRideDetailData(userSumData, from,
                to,
                Calendar.getInstance(),
                4);
        mCreateRideDetailData[3] = new CreateRideDetailData(userSumData, from,
                to,
                Calendar.getInstance(),
                4);
        mCreateRideDetailData[4] = new CreateRideDetailData(userSumData, from,
                to,
                Calendar.getInstance(),
                4);
        return mCreateRideDetailData;
    }

    @Override
    protected CreateRideDetailData[] doInBackground(String... strings) {
        Log.d(this.getClass().getName(), "------Getting Possible Rides----------" + strings[0]);
        return new CreateRideDetailData[0];
    }

    @Override
    protected void onPostExecute(CreateRideDetailData[] createRideDetailData) {
//        mGeoDataClient = Places.getGeoDataClient(mSearchRideActivity.getApplicationContext());
//        mGeoDataClient.getPlaceById("ChIJL_P_CXMEDTkRw0ZdG-0GVvw").addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
//            @Override
//            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
//                if(task.isSuccessful()){
//                    PlaceBufferResponse places = task.getResult();
//                    Place place = places.get(0);
//                    mCreateRideDetailData = tempo(new PlaceData(place.getName().toString(), place.getAddress().toString(), place.getId(), place.getLatLng()),
//                            new PlaceData(place.getName().toString(), place.getAddress().toString(), place.getId(), place.getLatLng()));
//                    mSearchRideActivity.swapPosData(mCreateRideDetailData);
//                    places.release();
//                }
//            }
//        });

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
                            mSearchRideActivity.swapPosData(new CreateRideDetailData[0]);
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
                            if (((Map<String, Object>)ride.get("to_loc"))
                                    .get("id")
                                    .toString()
                                    .contentEquals(mSearchRideActivity.getTovalue().getId())
                                    ) {

                                //TODO
                                //1. convert the received data into Create Ride Detail Data Object
                                //2. swap the adapter with the received data
                                Log.d(this.getClass().getName(), "************** CALLING MAP FROM RIDE DETAIL TASK *************************");
                                createRideDetailDataArrayList.add(new CreateRideDetailData(entry.getKey(), (Map<String, Object>)entry.getValue()));
                            }
                        }

                        mCreateRideDetailData = new CreateRideDetailData[createRideDetailDataArrayList.size()];
                        for(int i = 0; i < createRideDetailDataArrayList.size(); ++i){
                            mCreateRideDetailData[i] = createRideDetailDataArrayList.get(i);
                        }

                        if(createRideDetailDataArrayList != null && createRideDetailDataArrayList.size() > 0) {
                            mSearchRideActivity.swapPosData(mCreateRideDetailData);
                        } else {
                            mSearchRideActivity.swapPosData(new CreateRideDetailData[0]);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}