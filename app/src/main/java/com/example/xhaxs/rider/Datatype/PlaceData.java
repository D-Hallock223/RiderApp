package com.example.xhaxs.rider.Datatype;

import java.io.Serializable;

public class PlaceData implements Serializable {

    private String mPlaceNameMain;
    private String mPlaceNameSecondary;

    public PlaceData() {
        this.mPlaceNameMain = "";
        this.mPlaceNameSecondary = "";
    }

    public PlaceData(String placeNameMain, String placeNameSecondary) {
        this.mPlaceNameMain = placeNameMain;
        this.mPlaceNameSecondary = placeNameSecondary;
    }

    public void setPlaceData(String PlaceNameMain) {
        this.mPlaceNameMain = PlaceNameMain;
    }

    public String getPlaceNameMain() {
        return this.mPlaceNameMain;
    }

    public String getPlaceNameSecondary() {
        return mPlaceNameSecondary;
    }

    public void setPlaceNameSecondary(String mPlaceNameSecondary) {
        this.mPlaceNameSecondary = mPlaceNameSecondary;
    }

    @Override
    public String toString() {
        return this.mPlaceNameMain + ", " + this.mPlaceNameSecondary;
    }
}
