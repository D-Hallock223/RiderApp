package com.example.xhaxs.rider.Datatype;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class PlaceData implements Parcelable {

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
        return getName();
    }
}
