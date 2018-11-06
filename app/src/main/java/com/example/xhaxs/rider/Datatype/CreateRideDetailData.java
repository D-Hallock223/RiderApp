package com.example.xhaxs.rider.Datatype;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateRideDetailData implements Parcelable {
    private static final String LOG_CLASS = CreateRideDetailData.class.getName();

    public static final String RIDE_OWNER_STRING = "ride_owner";
    public static final String TO_LOC_STRING = "to_loc";
    public static final String FROM_LOC_STRING = "from_loc";
    public static final String RIDE_USER_ARRAY_STRING = "ride_users";
    public static final String JOURNEY_TIME_STRING = "journey_time";
    public static final String MAX_ACC_STRING = "max_accomodation";
    public static final String RIDE_FINISH_STRING = "is_ride_finish";
    public static final String RIDE_CANCELLED_STRING = "is_ride_cancelled";

    public static final int RIDE_UNDONE = -1;
    public static final int RIDE_FINSISHED = 1;

    public static final int RIDE_CANCELLED = 2;

    private String rideID;

    public String getRideID() {
        return rideID;
    }

    public void setRideID(String rideID) {
        this.rideID = rideID;
    }

    private UserSumData rideOwner;
    private PlaceData toLoc;
    private PlaceData fromLoc;
    private ArrayList<UserSumData> rideUsers;
    private long journeyTime;
    private int maxAccomodation;
    private int rideFinished;

    public CreateRideDetailData(Parcel in){
        this.rideID = in.readString();
        rideOwner = in.readParcelable(UserSumData.class.getClassLoader());
        toLoc = in.readParcelable(PlaceData.class.getClassLoader());
        fromLoc = in.readParcelable(PlaceData.class.getClassLoader());
        this.rideUsers = in.readArrayList(UserSumData.class.getClassLoader());
        journeyTime = in.readLong();
        maxAccomodation = in.readInt();
        rideFinished = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.rideID);
        dest.writeParcelable(rideOwner, flags);
        dest.writeParcelable(toLoc, flags);dest.writeParcelable(fromLoc, flags);
        dest.writeList(rideUsers);
        dest.writeLong(journeyTime);
        dest.writeInt(maxAccomodation);
        dest.writeInt(rideFinished);
    }

    public CreateRideDetailData(UserSumData rideOwner, PlaceData toLoc, PlaceData fromLoc, Calendar journeyTime, int maxAccomodation) {
        this.rideID = "";
        this.rideUsers = new ArrayList<>();
        this.rideUsers.add(rideOwner);
        this.rideOwner = rideOwner;
        this.toLoc = toLoc;
        this.fromLoc = fromLoc;
        this.journeyTime = journeyTime.getTimeInMillis();
        this.maxAccomodation = maxAccomodation;
        this.rideFinished = RIDE_UNDONE;
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

    public boolean addUser(UserSumData cUserData) {
        if(cUserData.getUid().equals(this.rideOwner.getUid())) return false;

        if(rideUsers.size() == maxAccomodation) return false;
        for(int i = 0; i < rideUsers.size(); ++i){
            if(cUserData.getUid().equals(rideUsers.get(i).getUid())) return false;
        }
        rideUsers.add(cUserData);
        return true;
    }

    public boolean removeUser(UserSumData cUserData){
        if(cUserData.getUid().equals(rideOwner.getUid())) return false;
        if(rideUsers.size() == 1) return false;
        for(int i = 0; i < rideUsers.size(); ++i){
            if(cUserData.getUid().equals(rideUsers.get(i).getUid())){
                rideUsers.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean removeUser(String uid){
        if(uid.equals(rideOwner.getUid())) return false;
        if(rideUsers.size() == 1) return false;
        for(int i = 0; i < rideUsers.size(); ++i){
            if(uid.equals(rideUsers.get(i).getUid())){
                rideUsers.remove(i);
                return true;
            }
        }
        return false;
    }



    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(RIDE_OWNER_STRING, rideOwner.toMap());
        map.put(FROM_LOC_STRING, fromLoc.toMap());
        map.put(TO_LOC_STRING, toLoc.toMap());
        map.put(JOURNEY_TIME_STRING, journeyTime);
        map.put(MAX_ACC_STRING, maxAccomodation);
        map.put(RIDE_FINISH_STRING, rideFinished);
        HashMap<String, Object> rideUsersHash = new HashMap<>();
        for(int i = 0; i < rideUsers.size(); ++i){
            rideUsersHash.put(rideUsers.get(i).getUid(), rideUsers.get(i).toMap());
        }
        map.put(RIDE_USER_ARRAY_STRING, rideUsersHash);
        Log.d("000","--------------------------");
        Log.d(LOG_CLASS, map.toString());
        Log.d("000","--------------------------");
        return map;
    }

    public CreateRideDetailData(String key, Map<String, Object> map){
        this.rideID = key;

        Log.d(this.getClass().getName(), "************** CALLING MAP FOR -- RIDE OWNER -- CREATE RIDE DETAIL");
        this.rideOwner = new UserSumData((Map<String, Object>)map.get(RIDE_OWNER_STRING));

        Log.d(this.getClass().getName(), "************** CALLING MAP FOR -- FROM PLACE -- CREATE RIDE DETAIL");
        this.fromLoc = new PlaceData((Map<String, Object>)map.get(FROM_LOC_STRING));

        Log.d(this.getClass().getName(), "************** CALLING MAP FOR -- TO PLACE -- CREATE RIDE DETAIL");
        this.toLoc = new PlaceData((Map<String, Object>)map.get(TO_LOC_STRING));

        Log.d(this.getClass().getName(), "************** CALLING MAP FOR -- MAX ACCOMODATION -- CREATE RIDE DETAIL");
        this.maxAccomodation = Integer.parseInt(map.get(MAX_ACC_STRING).toString());

        Log.d(this.getClass().getName(), "************** CALLING MAP FOR -- JOURNEY TIME -- CREATE RIDE DETAIL");
        this.journeyTime = Long.parseLong(map.get(JOURNEY_TIME_STRING).toString());

        Log.d(this.getClass().getName(), "************** CALLING MAP FOR -- RIDE FINISH -- CREATE RIDE DETAIL");
        this.rideFinished = Integer.parseInt(map.get(RIDE_FINISH_STRING).toString());

        Log.d(this.getClass().getName(), "************** CALLING MAP FOR -- USER ARRAY DATA -- CREATE RIDE DETAIL");
        Map<String, Object> ru = (Map<String,Object>) map.get(RIDE_USER_ARRAY_STRING);

        this.rideUsers = new ArrayList<>();

        for(Map.Entry<String, Object> entry : ru.entrySet()){
            UserSumData temp = new UserSumData((Map<String, Object>)entry.getValue());
            this.rideUsers.add(temp);
        }
    }

    public boolean isOwner(String id){
        return (rideOwner.getUid().equals(id) ? true : false);
    }

    public boolean isMember(String id){
        if(rideOwner.getUid().equals(id)) return true;
        for(int i = 0; i < rideUsers.size(); ++i){
            if(rideUsers.get(i).getUid().equals(id)) return true;
        }
        return false;
    }

    public int getRideFinished() {
        return rideFinished;
    }

    public boolean setRideFinished(int rideFinished) {
        if(this.rideFinished == rideFinished) return false;
        this.rideFinished = rideFinished;
        return true;
    }
}
