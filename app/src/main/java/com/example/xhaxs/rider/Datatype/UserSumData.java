package com.example.xhaxs.rider.Datatype;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.HashMap;

public class UserSumData implements Parcelable {
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
        map.put("uid", this.uid);
        map.put("name", this.uname);
        map.put("email", this.email);
        return map;
    }
}
