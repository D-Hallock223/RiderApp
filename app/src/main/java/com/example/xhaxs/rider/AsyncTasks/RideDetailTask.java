package com.example.xhaxs.rider.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.xhaxs.rider.Activity.SearchRideActivity;
import com.example.xhaxs.rider.Datatype.CreateRideDetailData;
import com.example.xhaxs.rider.Datatype.PlaceData;
import com.example.xhaxs.rider.Datatype.UserSumData;

import java.util.Calendar;

public class RideDetailTask extends AsyncTask<String, Void, CreateRideDetailData[]> {

    private SearchRideActivity mSearchRideActivity;
    private CreateRideDetailData[] mCreateRideDetailData;

    public RideDetailTask(SearchRideActivity searchRideActivity) {
        mSearchRideActivity = searchRideActivity;
    }

    private CreateRideDetailData[] tempo() {
        UserSumData userSumData = new UserSumData("1", "temp", "temp@gmail.com");
        mCreateRideDetailData = new CreateRideDetailData[5];
        mCreateRideDetailData[0] = new CreateRideDetailData(userSumData, new PlaceData("delhi", "Delhi"),
                new PlaceData("bombay", "Bombay"),
                Calendar.getInstance(),
                4);
        mCreateRideDetailData[1] = new CreateRideDetailData(userSumData, new PlaceData("delhi", "Delhi"),
                new PlaceData("bombay", "Bombay"),
                Calendar.getInstance(),
                4);
        mCreateRideDetailData[2] = new CreateRideDetailData(userSumData, new PlaceData("delhi", "Delhi"),
                new PlaceData("bombay", "Bombay"),
                Calendar.getInstance(),
                4);
        mCreateRideDetailData[3] = new CreateRideDetailData(userSumData, new PlaceData("delhi", "Delhi"),
                new PlaceData("bombay", "Bombay"),
                Calendar.getInstance(),

                4);
        mCreateRideDetailData[4] = new CreateRideDetailData(userSumData, new PlaceData("delhi", "Delhi"),
                new PlaceData("bombay", "Bombay"),
                Calendar.getInstance(),
                4);
        return mCreateRideDetailData;
    }

    @Override
    protected CreateRideDetailData[] doInBackground(String... strings) {
        Log.d(this.getClass().getName(), "------Getting Possible Rides----------" + strings[0]);
        return tempo();
    }

    @Override
    protected void onPostExecute(CreateRideDetailData[] createRideDetailData) {
        mSearchRideActivity.swapPosData(createRideDetailData);
    }
}
