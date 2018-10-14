package com.example.xhaxs.rider.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.xhaxs.rider.Activity.CreateRideActivity;
import com.example.xhaxs.rider.Datatype.PlaceData;
import com.example.xhaxs.rider.Network.PlaceNetJSON;

public class PlaceTask extends AsyncTask<String, Void, PlaceData[]> {

    private CreateRideActivity mCreateRiderActivity = null;
    private PlaceNetJSON placeNetJSON;

    public PlaceTask(CreateRideActivity createRideActivity) {
        super();
        mCreateRiderActivity = createRideActivity;
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
        placeNetJSON = new PlaceNetJSON(strings[0]);
        return placeNetJSON.openConnect();
    }

    @Override
    protected void onPostExecute(PlaceData[] placeData) {
        super.onPostExecute(placeData);
        mCreateRiderActivity.swapAutoCompleteData(placeData);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        placeNetJSON.cancelRequest();
    }
}
