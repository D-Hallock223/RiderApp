package com.example.xhaxs.rider.Datatype;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class UserSumData implements Parcelable {

    public static final String UID_STRING = "uid";
    public static final String UNAME_STRING = "uname";
    public static final String EMAIL_STRING = "email";

    private String uid;
    private String uname;
    private String email;

    public UserSumData(String uid, String uname, String email) {
        this.uid = uid;
        this.uname = uname;
        this.email = email;
    }

    public UserSumData(Parcel in){
        uid = in.readString();
        uname = in.readString();
        email = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.uname);
        dest.writeString(this.email);
    }

    public static final Parcelable.Creator<UserSumData> CREATOR
            = new Parcelable.Creator<UserSumData>() {
        public UserSumData createFromParcel(Parcel in) {
            return new UserSumData(in);
        }

        public UserSumData[] newArray(int size) {
            return new UserSumData[size];
        }
    };


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NonNull
    @Override
    public String toString() {
        return "<UserSumData ->\n" +
                "UID # " + uid + "\n" +
                "Email # " + email + "\n" +
                "Name # " + uname + ">";
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(UID_STRING, this.uid);
        map.put(UNAME_STRING, this.uname);
        map.put(EMAIL_STRING, this.email);
        return map;
    }

    public UserSumData(Map<String, Object> map){

        Log.d(this.getClass().getName(), "************** CALLING MAP FOR -- ID -- USER SUM DATA");
        this.uid = map.get(UID_STRING).toString();

        Log.d(this.getClass().getName(), "************** CALLING MAP FOR -- UNAME -- USER SUM DATA");
        this.uname = map.get(UNAME_STRING).toString();

        Log.d(this.getClass().getName(), "************** CALLING MAP FOR -- EMAIL -- USER SUM DATA");
        this.email = map.get(EMAIL_STRING).toString();
    }
}
