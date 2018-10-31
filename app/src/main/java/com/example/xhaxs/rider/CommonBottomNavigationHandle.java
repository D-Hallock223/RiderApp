package com.example.xhaxs.rider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.xhaxs.rider.Activity.CreateRideActivity;
import com.example.xhaxs.rider.Activity.MyRidesActivity;
import com.example.xhaxs.rider.Activity.SearchRideActivity;

public class CommonBottomNavigationHandle {
    public static void chooseSelectedActivity(Activity activity, MenuItem item){
//        if(matchCurrent(activity.getApplicationContext(), item)) {
//            Toast.makeText(activity.getApplicationContext(), "Matched!", Toast.LENGTH_SHORT).show();
//            return;
//        }
        Intent intent = null;
        switch (item.getItemId()){
            case R.id.mi_create_ride:
                if(activity.getClass().getName().equals(CreateRideActivity.class.getName())) return;
                intent = new Intent(activity.getApplicationContext(), CreateRideActivity.class);
                break;
            case R.id.mi_search_ride:
                if(activity.getClass().getName().equals(SearchRideActivity.class.getName())) return;
                intent = new Intent(activity.getApplicationContext(), SearchRideActivity.class);
                break;
            case R.id.mi_my_ride:
                if(activity.getClass().getName().equals(MyRidesActivity.class.getName())) return;
                intent = new Intent(activity.getApplicationContext(), MyRidesActivity.class);
                break;
        }
        activity.startActivity(intent);
        activity.finish();
        return;
    }

}
