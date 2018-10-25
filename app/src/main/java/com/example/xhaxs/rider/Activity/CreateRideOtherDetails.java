package com.example.xhaxs.rider.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.xhaxs.rider.Datatype.CreateRideDetailData;
import com.example.xhaxs.rider.Datatype.PlaceData;
import com.example.xhaxs.rider.Datatype.UserSumData;
import com.example.xhaxs.rider.R;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

//import java.text.DateFormat;

public class CreateRideOtherDetails extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final String FINAL_MAX_COUNT_DISPLAY = "MAX_COUNT_DISPLAY";
    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    private FirebaseUser firebaseUser;
    private ImageButton mpick;
    private TextView mDisplay;
    private TextView mIncMaxCount;
    private TextView mDecMaxCount;
    private TextView mMaxCountDisplay;
    private int cMaxCount;
    private TextView mFromLocatioFinalMain;
    private TextView mToLocationFinalMain;
    private TextView mFromLocationSecondary;
    private TextView mToLocationSecondary;
    private Button mSubmitRideDetails;
    private TextView mRecapDetails;
    private PlaceData fromPlaceData;
    private PlaceData toPlaceData;
    private Calendar calendarFinal;
    private CreateRideDetailData mCreateRideDetailData;
    private DatabaseReference mDatabase;
    private GeoDataClient mGeoDataClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride_other_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        mFromLocatioFinalMain = findViewById(R.id.tv_from_location_final);
        mToLocationFinalMain = findViewById(R.id.tv_to_location_final);
        mFromLocationSecondary = findViewById(R.id.tv_from_extra_info_cro);
        mToLocationSecondary = findViewById(R.id.tv_to_extra_info_cro);

        mGeoDataClient = Places.getGeoDataClient(this);
        final String fLoc = intent.getStringExtra("fromLocationDetails");
        final String tLoc = intent.getStringExtra("toLocationDetails");

        Log.d(this.getClass().getName(), "+++++++++++++++++++GOT FROM -- " + fLoc + " <><><> " + tLoc + " -- TO ID++++++++++++++++++++++++++++");

        mGeoDataClient.getPlaceById(fLoc).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                if(task.isSuccessful()){
                    Log.d(this.getClass().getName(), "-**************************** --------> Getting From Loc <------- ********************-");
                    PlaceBufferResponse places = task.getResult();
                    Place p = places.get(0);
                    fromPlaceData = new PlaceData(p.getName().toString(), p.getAddress().toString(), p.getId(), p.getLatLng());
                    Log.d(this.getClass().getName(), "-****************************GOT From Loc -> " + fromPlaceData.getName() + "********************-");
                    mFromLocatioFinalMain.setText(fromPlaceData.getName());
                    mFromLocationSecondary.setText(fromPlaceData.getAddress());
                    places.release();
                }
            }
        });

        mGeoDataClient.getPlaceById(tLoc).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                if(task.isSuccessful()){
                    Log.d(this.getClass().getName(), "-**************************** --------> Getting To Loc <------- ********************-");
                    PlaceBufferResponse places = task.getResult();
                    Place p = places.get(0);
                    toPlaceData = new PlaceData(p.getName().toString(), p.getAddress().toString(), p.getId(), p.getLatLng());
                    Log.d(this.getClass().getName(), "-****************************GOT From Loc -> " + toPlaceData.getName() + "********************-");
                    mToLocationFinalMain.setText(toPlaceData.getName());
                    mToLocationSecondary.setText(toPlaceData.getAddress());
                    places.release();
                }
            }
        });


//        mFromLocatioFinalMain.setText(fromPlaceData.getPlaceNameMain());
//        mFromLocationSecondary.setText(fromPlaceData.getPlaceNameSecondary());

//        mToLocationFinalMain.setText(toPlaceData.getPlaceNameMain());
//        mToLocationSecondary.setText(toPlaceData.getPlaceNameSecondary());

        mpick = findViewById(R.id.b_pick_cr);
        mDisplay = findViewById(R.id.tv_date_time_cr);
        Calendar calendar = Calendar.getInstance();
        calendarFinal = calendar;

        java.text.DateFormat dateFormat = java.text.DateFormat
                .getDateTimeInstance(java.text.DateFormat.SHORT, java.text.DateFormat.SHORT);
        String str = dateFormat.format(calendar.getTime());
        mDisplay.setText(str);

        mIncMaxCount = findViewById(R.id.b_inc_max_count);
        mDecMaxCount = findViewById(R.id.b_dec_max_count);
        mMaxCountDisplay = findViewById(R.id.tv_max_count_cr);
        mRecapDetails = findViewById(R.id.tv_final_details_recap);

        cMaxCount = 1;
        Toast.makeText(this, "Current mMaxCount -> " + cMaxCount, Toast.LENGTH_SHORT).show();
        mMaxCountDisplay.setText("" + cMaxCount);

        mSubmitRideDetails = findViewById(R.id.b_submit_cr);

        mpick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateRideOtherDetails.this, CreateRideOtherDetails.this,
                        year, month, day);
                datePickerDialog.show();
            }
        });

        mIncMaxCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCount(1);
            }
        });

        mDecMaxCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCount(-1);
            }
        });

        mSubmitRideDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* TODO
                 *
                 * 1. Replace UserSumData in Create RideDetail with user id of current User
                 * 2. Store the data in Firebase
                 *
                 */

                // ...

                mCreateRideDetailData = new CreateRideDetailData(
                        new UserSumData("1", "Name", "name@gmail.com"),
                        fromPlaceData,
                        toPlaceData,
                        calendarFinal,
                        cMaxCount
                );

                mDatabase = FirebaseDatabase.getInstance().getReference();
                String key = mDatabase.child("Riders").push().getKey();
                HashMap<String, Object> result = mCreateRideDetailData.toMap();
//                result.put("from", fromPlaceData.getName());
//                result.put("to", toPlaceData.getName());
//                result.put("date", mDisplay.getText().toString());
//                result.put("maxrides", mMaxCountDisplay.getText().toString());
                //          mDatabase.child("Riders").setValue(result);
                //   mDatabase.child("Riders").push(result);
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/Riders/" + key, result);
                mDatabase.updateChildren(childUpdates);




                Toast.makeText(CreateRideOtherDetails.this, "Rider Created with details # " + mCreateRideDetailData.toString(), Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(CreateRideOtherDetails.this, SearchRideActivity.class);
                startActivity(intent1);
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(FINAL_MAX_COUNT_DISPLAY, cMaxCount);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.i(this.getClass().getName(), "----------------------------------YEAR --- " + year);
        yearFinal = year;
        monthFinal = month;
        dayFinal = dayOfMonth;

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateRideOtherDetails.this, CreateRideOtherDetails.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hourFinal = hourOfDay;
        minuteFinal = minute;
        Calendar calendar = new GregorianCalendar(yearFinal, monthFinal, dayFinal, hourFinal, monthFinal);
        calendarFinal = calendar;
        java.text.DateFormat dateFormat = java.text.DateFormat
                .getDateTimeInstance(java.text.DateFormat.SHORT, java.text.DateFormat.SHORT);
        String str = dateFormat.format(calendar.getTime());
        mDisplay.setText(str);
    }

    private void changeCount(int v) {
        cMaxCount += v;
        if (cMaxCount < 0) cMaxCount = 0;
        mMaxCountDisplay.setText("" + cMaxCount);
    }
}
