package com.example.xhaxs.rider.Datatype;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PlaceData implements Parcelable {

    public static final String NAME_STRING = "name";
    public static final String ADDRESS_STRING = "address";
    public static final String ID_STRING = "id";
    public static final String LAT_STRING = "latitude";
    public static final String LNG_STRING = "longitude";

    private String mName;
    private String mAddress;
    private String mID;
    private LatLng mLatLng;

    public PlaceData(String mName, String mAddress, String mID, LatLng mLatLng) {
        this.mName = mName;
        this.mAddress = mAddress;
        this.mID = mID;
        this.mLatLng = mLatLng;
    }

    public PlaceData(Parcel in){
        this.mName = in.readString();
        this.mAddress = in.readString();
        this.mID = in.readString();
        this.mLatLng = in.readParcelable(LatLng.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mAddress);
        dest.writeString(mID);
        dest.writeParcelable(this.mLatLng, flags);
    }

    public static final Parcelable.Creator<PlaceData> CREATOR
            = new Parcelable.Creator<PlaceData>() {
        public PlaceData createFromParcel(Parcel in) {
            return new PlaceData(in);
        }

        public PlaceData[] newArray(int size) {
            return new PlaceData[size];
        }
    };


    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getID() {
        return mID;
    }

    public void setID(String mID) {
        this.mID = mID;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLatLng(LatLng mLatLng) {
        this.mLatLng = mLatLng;
    }

    @NonNull
    @Override
    public String toString() {
        return getAddress();
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(ID_STRING, this.mID);
        map.put(NAME_STRING, this.mName);
        map.put(ADDRESS_STRING, this.mAddress);
        map.put(LAT_STRING, this.mLatLng.latitude);
        map.put(LNG_STRING, this.mLatLng.longitude);
        return map;
    }

    public PlaceData(Map<String, Object> map){
        Log.d(this.getClass().getName(), "************** CALLING MAP FOR -- ID -- PLACE DATA");
        this.mID = map.get(ID_STRING).toString();

        Log.d(this.getClass().getName(), "************** CALLING MAP FOR -- NAME -- PLACE DATA");
        this.mName = map.get(NAME_STRING).toString();

        Log.d(this.getClass().getName(), "************** CALLING MAP FOR -- ADDRESS -- PLACE DATA");
        this.mAddress = map.get(ADDRESS_STRING).toString();

        Log.d(this.getClass().getName(), "************** CALLING MAP FOR -- LAT LNG-- PLACE DATA");
        this.mLatLng = new LatLng(
                (double)Double.parseDouble(map.get(LAT_STRING).toString()),
                (double)Double.parseDouble(map.get(LNG_STRING).toString())
        );
    }
}
