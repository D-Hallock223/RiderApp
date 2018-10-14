package com.example.xhaxs.rider.Datatype;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.io.Serializable;

public class CUserData implements Serializable {
    private long uid;
    private String email;
    private String uname;
    private String countryCode;
    private String contact;
    private Bitmap bitmap;

    public CUserData(long uid, String email, String uname, String countryCode, String contact, Bitmap bitmap) {
        this.uid = uid;
        this.email = email;
        this.uname = uname;
        this.countryCode = countryCode;
        this.contact = contact;
        this.bitmap = bitmap;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @NonNull
    @Override
    public String toString() {
        return "<CUserData -> " + "\n" +
                "Email # " + getEmail() + "\n" +
                "Name # " + getUname() + "\n" +
                "Contact # " + getContact() + ">";
    }
}
