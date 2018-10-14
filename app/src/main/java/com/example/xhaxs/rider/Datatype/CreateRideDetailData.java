package com.example.xhaxs.rider.Datatype;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class CreateRideDetailData implements Serializable {


    private UserSumData rideOwner;
    private PlaceData toLoc;
    private PlaceData fromLoc;
    private ArrayList<UserSumData> rideUsers;
    private Calendar journeyTime;
    private int maxAccomodation;
    private int curAccomodation;

    public CreateRideDetailData() {
    }

    public CreateRideDetailData(UserSumData rideOwner, PlaceData toLoc, PlaceData fromLoc, Calendar journeyTime, int maxAccomodation) {
        setRideOwner(rideOwner);
        this.toLoc = toLoc;
        this.fromLoc = fromLoc;
        this.journeyTime = journeyTime;
        this.maxAccomodation = maxAccomodation;
    }

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
        setRideUsers(new ArrayList<UserSumData>());
        addUser(rideOwner);
    }

    public PlaceData getFromLoc() {
        return fromLoc;
    }

    public void setFromLoc(PlaceData fromLoc) {
        this.fromLoc = fromLoc;
    }

    public Calendar getJourneyTime() {
        return journeyTime;
    }

    public void setJourneyTime(Calendar journeyTime) {
        this.journeyTime = journeyTime;
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
        return curAccomodation;
    }

    public void setCurAccomodation(int curAccomodation) {
        this.curAccomodation = getRideUsers().size();
    }

    @Override
    public String toString() {
        return "<CreateRideDetailDate -> " + "\n"
                + "From: " + fromLoc.toString() + "\n"
                + "To: " + toLoc.toString() + "\n"
                + "Time: " + DateFormat
                .getDateTimeInstance(DateFormat.SHORT
                        , DateFormat.SHORT)
                .format(journeyTime.getTime()) + "\n"
                + "maxAccomodation: " + maxAccomodation
                + "> ";
    }

    public void addUser(UserSumData cUserData) {
        rideUsers.add(cUserData);
        setCurAccomodation(getRideUsers().size());
    }

    public void removeUse(UserSumData cUserData) {
        Iterator<UserSumData> cUserDataIterator = rideUsers.iterator();
        while (cUserDataIterator.hasNext()) {
            UserSumData cUserData1 = cUserDataIterator.next();
            if (cUserData1.getUid() == cUserData.getUid()) {
                cUserDataIterator.remove();
                setCurAccomodation(getRideUsers().size());
                return;
            }
        }
    }
}
