package com.example.xhaxs.rider.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.xhaxs.rider.Activity.SearchRideActivity;
import com.example.xhaxs.rider.Datatype.PlaceData;
import com.example.xhaxs.rider.Network.PlaceNetJSON;

public class PlaceTaskSR extends AsyncTask<String, Void, PlaceData[]> {

    private SearchRideActivity mSearchRideActivity = null;

    public PlaceTaskSR(SearchRideActivity searchRideActivty) {
        super();
        mSearchRideActivity = searchRideActivty;
    }

    private PlaceData[] tempo() {
        PlaceData[] pt = new PlaceData[5];
        pt[0] = new PlaceData("Delhi", "India");
        pt[1] = new PlaceData("New Delhi", "India");
        pt[2] = new PlaceData("Cannaut Place", "New Delhi, India");
        pt[3] = new PlaceData("Ring Road", "Delhi, India");
        pt[4] = new PlaceData("Jantar Mantar", "Delhi, India");
        return pt;
    }

    @Override
    protected PlaceData[] doInBackground(String... strings) {
        Log.d(this.getClass().getName(), "-----------------------" + strings[0] + "-----------------------------");
        return new PlaceNetJSON(strings[0]).openConnect();
    }

    @Override
    protected void onPostExecute(PlaceData[] placeData) {
        super.onPostExecute(placeData);
        mSearchRideActivity.swapAutoCompleteData(placeData);
    }
}
