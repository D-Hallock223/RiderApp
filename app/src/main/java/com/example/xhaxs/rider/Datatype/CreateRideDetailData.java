package com.example.xhaxs.rider.Datatype;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.xhaxs.rider.Activity.CreateRideOtherDetails;
import com.google.android.gms.location.places.Place;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class CreateRideDetailData implements Parcelable {
    private static final String LOG_CLASS = CreateRideDetailData.class.getName();

    private UserSumData rideOwner;
    private PlaceData toLoc;
    private PlaceData fromLoc;
    private ArrayList<UserSumData> rideUsers;
    private long journeyTime;
    private int maxAccomodation;

    public CreateRideDetailData(Parcel in){
        Log.d(LOG_CLASS, "***************Reading value *************** rideOwner...");
        rideOwner = in.readParcelable(UserSumData.class.getClassLoader());
        Log.d(LOG_CLASS, "***************Reading value *************** toLoc...");
        toLoc = in.readParcelable(PlaceData.class.getClassLoader());
        Log.d(LOG_CLASS, "***************Reading value *************** fromLoc...");
        fromLoc = in.readParcelable(PlaceData.class.getClassLoader());
        Log.d(LOG_CLASS, "***************Reading value *************** rideUser...");
        this.rideUsers = in.readArrayList(UserSumData.class.getClassLoader());
        Log.d(LOG_CLASS, "***************Reading value *************** journey time ...");
        journeyTime = in.readLong();
        Log.d(LOG_CLASS, "***************Reading value *************** maxAccomodation...");
        maxAccomodation = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.d(LOG_CLASS, "***************Writing value *************** rideOwner...");
        dest.writeParcelable(rideOwner, flags);
        Log.d(LOG_CLASS, "***************Writing value *************** toLoc...");
        dest.writeParcelable(toLoc, flags);
        Log.d(LOG_CLASS, "***************Writing value *************** fromLoc...");
        dest.writeParcelable(fromLoc, flags);
        Log.d(LOG_CLASS, "***************Writing value *************** rideUsers...");
        dest.writeList(rideUsers);
        Log.d(LOG_CLASS, "***************Writing value *************** rideJourney...");
        dest.writeLong(journeyTime);
        Log.d(LOG_CLASS, "***************Writing value *************** maxAccomdation...");
        dest.writeInt(maxAccomodation);
    }

    public CreateRideDetailData(UserSumData rideOwner, PlaceData toLoc, PlaceData fromLoc, Calendar journeyTime, int maxAccomodation) {
        this.rideUsers = new ArrayList<>();
        this.rideUsers.add(rideOwner);
        this.rideOwner = rideOwner;
        this.toLoc = toLoc;
        this.fromLoc = fromLoc;
        this.journeyTime = journeyTime.getTimeInMillis();
        this.maxAccomodation = maxAccomodation;
        Log.d(this.getClass().getName(), "----***-----***----" + this.toLoc.toString() + " <---> " + this.fromLoc.toString() + "-0---0000---0000----");
    }

    public static final Creator<CreateRideDetailData> CREATOR = new Creator<CreateRideDetailData>() {
        public CreateRideDetailData createFromParcel(Parcel source) {
            return new CreateRideDetailData(source);
        }
        public CreateRideDetailData[] newArray(int size) {
            return new CreateRideDetailData[size];
        }
    };

    public PlaceData getToLoc() {
        return toLoc;
    }

    public void setToLoc(PlaceData toLoc) {
        this.toLoc = toLoc;
    }

    public UserSumData getRideOwner() {
        return rideOwner;
    }

    public void setRideOwner(UserSumData rideOwner) {
        this.rideOwner = rideOwner;
    }

    public PlaceData getFromLoc() {
        return fromLoc;
    }

    public void setFromLoc(PlaceData fromLoc) {
        this.fromLoc = fromLoc;
    }

    public Calendar getJourneyTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(journeyTime);
        return calendar;
    }

    public void setJourneyTime(Calendar journeyTime) {
        this.journeyTime = journeyTime.getTimeInMillis();
    }

    public ArrayList<UserSumData> getRideUsers() {
        return rideUsers;
    }

    public void setRideUsers(ArrayList<UserSumData> rideUsers) {
        this.rideUsers = rideUsers;
    }

    public int getMaxAccomodation() {
        return maxAccomodation;
    }

    public void setMaxAccomodation(int maxAccomodation) {
        this.maxAccomodation = maxAccomodation;
    }

    public int getCurAccomodation() {
        return (this.rideUsers.size());
    }

    @Override
    public String toString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(journeyTime);
        return "<CreateRideDetailDate -> " + "\n"
                + "From: " + fromLoc.getName() + "\n"
                + "To: " + toLoc.getName() + "\n"
                + "Time: " + DateFormat
                .getDateTimeInstance(DateFormat.SHORT
                        , DateFormat.SHORT)
                .format(calendar.getTime()) + "\n"
                + "maxAccomodation: " + maxAccomodation
                + "> ";
    }

    public void addUser(UserSumData cUserData) {
        if(rideUsers.size() == maxAccomodation) return;
        rideUsers.add(cUserData);
    }
}
