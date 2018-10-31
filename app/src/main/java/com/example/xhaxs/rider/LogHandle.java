package com.example.xhaxs.rider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.xhaxs.rider.Activity.LoginActivity;
import com.example.xhaxs.rider.Activity.SearchRideActivity;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public final class LogHandle {

    public static FirebaseUser checkLogin(FirebaseAuth firebaseAuth, Activity activity){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(activity.getApplicationContext(), LoginActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
        return currentUser;
    }

    public static void LogoutMenuInflate(){

    }

    public static void logout(FirebaseAuth mAuth, Activity activity) {

        mAuth.signOut();
        LoginManager.getInstance().logOut();
        Intent sendtologin = new Intent(activity.getApplicationContext(), LoginActivity.class);
        activity.startActivity(sendtologin);
        activity.finish();

    }
}
