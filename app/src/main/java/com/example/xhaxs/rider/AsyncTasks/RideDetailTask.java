package com.example.xhaxs.rider.AsyncTasks;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.xhaxs.rider.Activity.SearchRideActivity;
import com.example.xhaxs.rider.Datatype.CreateRideDetailData;
import com.example.xhaxs.rider.Datatype.PlaceData;
import com.example.xhaxs.rider.Datatype.UserSumData;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;

public class RideDetailTask extends AsyncTask<String, Void, CreateRideDetailData[]> {

    private SearchRideActivity mSearchRideActivity;
    private CreateRideDetailData[] mCreateRideDetailData;

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
        mGeoDataClient = Places.getGeoDataClient(mSearchRideActivity.getApplicationContext());
        mGeoDataClient.getPlaceById("ChIJL_P_CXMEDTkRw0ZdG-0GVvw").addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                if(task.isSuccessful()){
                    PlaceBufferResponse places = task.getResult();
                    Place place = places.get(0);
                    mCreateRideDetailData = tempo(new PlaceData(place.getName().toString(), place.getAddress().toString(), place.getId(), place.getLatLng()),
                            new PlaceData(place.getName().toString(), place.getAddress().toString(), place.getId(), place.getLatLng()));
                    mSearchRideActivity.swapPosData(mCreateRideDetailData);
                    places.release();
                }
            }
        });
    }
}
