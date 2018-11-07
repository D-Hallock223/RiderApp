package com.example.xhaxs.rider;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.xhaxs.rider.Activity.LoginActivity;
import com.example.xhaxs.rider.Activity.PhoneNumberActivity;
import com.example.xhaxs.rider.Activity.ProfileDetailsActivity;
import com.example.xhaxs.rider.Activity.SearchRideActivity;
import com.example.xhaxs.rider.Datatype.UserSumData;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import javax.crypto.interfaces.PBEKey;

public final class LogHandle {

     public static Map<String, Object> mapCache = null;
     public static ProgressBar spinner;

    public static FirebaseUser checkLogin(FirebaseAuth firebaseAuth, Activity activity){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(activity.getApplicationContext(), LoginActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
        return currentUser;
    }

    public static void checkDetailsAdded(final FirebaseUser firebaseUser, final Activity activity){

        spinner = (ProgressBar) activity.findViewById(R.id.progress_bar_login_done);
        if(spinner != null) spinner.setVisibility(View.VISIBLE);

        Log.d("-23-23-23", "Got details for log handle!-23-234-234-234-234-24");

        if(mapCache != null){
            checkPhoneAuth(firebaseUser, activity);
            return;
        }

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("Users/").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                if(map == null){
                    Intent intent = new Intent(activity.getApplicationContext(), ProfileDetailsActivity.class);
                    Toast.makeText(activity.getApplicationContext(), "Please Complete Your Details", Toast.LENGTH_LONG).show();
                    activity.startActivity(intent);
                    activity.finish();
                    return;
                } else {
                    Log.d("------", "Map Cache set=================");
                    mapCache = map;
                    if(mapCache != null){
                        Log.d("------", "Map Cache set=> " + mapCache.toString());

                        checkPhoneAuth(firebaseUser, activity);
                    }
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void logout(FirebaseAuth mAuth, Activity activity) {

        mAuth.signOut();
        LoginManager.getInstance().logOut();
        flushCache();
        Intent sendtologin = new Intent(activity.getApplicationContext(), LoginActivity.class);
        activity.startActivity(sendtologin);
        activity.finish();

    }


    public static void flushCache(){
        mapCache = null;
    }

    public static void checkPhoneAuth(FirebaseUser firebaseUser, Activity activity){

        if(mapCache.get(AppUtils.PHONE_VERIFIED_STRING) == null ||
                ( mapCache.get(AppUtils.PHONE_VERIFIED_STRING) != null && mapCache.get(AppUtils.PHONE_VERIFIED_STRING).toString().equals("false"))){

            Intent intent = new Intent(activity.getApplicationContext(), PhoneNumberActivity.class);
            intent.putExtra(AppUtils.CURRENT_USER_STRING, new UserSumData(
                    firebaseUser.getUid(),
                    firebaseUser.getDisplayName(),
                    firebaseUser.getEmail())
            );
            activity.startActivity(intent);
            activity.finish();
        }

        if(spinner != null)
            spinner.setVisibility(View.GONE);
    }
}
