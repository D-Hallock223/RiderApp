package com.example.xhaxs.rider.Datatype;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class UserSumData implements Serializable {
    private String uid;
    private String uname;
    private String email;

    public UserSumData(String uid, String uname, String email) {
        this.uid = uid;
        this.uname = uname;
        this.email = email;
    }

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
}
