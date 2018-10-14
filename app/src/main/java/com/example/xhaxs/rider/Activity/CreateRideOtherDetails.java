package com.example.xhaxs.rider.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.GregorianCalendar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride_other_details);

//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if(firebaseUser == null){
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//            finish();
//        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        fromPlaceData = (PlaceData) intent.getSerializableExtra("fromLocationDetails");
        toPlaceData = (PlaceData) intent.getSerializableExtra("toLocationDetails");

        mFromLocatioFinalMain = findViewById(R.id.tv_from_location_final);
        mToLocationFinalMain = findViewById(R.id.tv_to_location_final);
        mFromLocationSecondary = findViewById(R.id.tv_from_extra_info_cro);
        mToLocationSecondary = findViewById(R.id.tv_to_extra_info_cro);

        mFromLocatioFinalMain.setText(fromPlaceData.getPlaceNameMain());
        mFromLocationSecondary.setText(fromPlaceData.getPlaceNameSecondary());

        mToLocationFinalMain.setText(toPlaceData.getPlaceNameMain());
        mToLocationSecondary.setText(toPlaceData.getPlaceNameSecondary());

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

                mCreateRideDetailData = new CreateRideDetailData(
                        new UserSumData("1", "Name", "name@gmail.com"),
                        fromPlaceData,
                        toPlaceData,
                        calendarFinal,
                        cMaxCount
                );

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
