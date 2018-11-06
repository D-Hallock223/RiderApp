package com.example.xhaxs.rider;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.MenuItem;

import com.squareup.picasso.Picasso;

public final class AppUtils {
    public static final String NETWORK_ERROR = "Network Not Available";
    public static final String USER_NAME_STRING = "userName";
    public static final String COUNTRY_CODE_STRING = "countryCode";
    public static final String PHONE_NUMBER_STRING = "phoneNumber";
    public static final String EMAIL_STRING = "email";
    public static final String GENDER_STRING = "gender";
    public static final String PHONE_VERIFIED_STRING = "phoneVerified";
    public static final String CURRENT_USER_STRING = "currentUser";
    public static final String PROFILE_IMAGE_FOLDER_STRING = "ProfileImages";
    public static final String PROFILE_PIC_URL_STRING = "profilePicURL";
//    public static final String CURRENT_USER_STRING = "currentUser";


    public static final boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static final void clearPicassoCache(Uri uri){
        if(uri == null) return;
        Picasso p = Picasso.get();
        p.invalidate(uri);
    }
}
