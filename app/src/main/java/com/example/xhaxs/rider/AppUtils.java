package com.example.xhaxs.rider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;

import com.facebook.login.widget.ProfilePictureView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.IOException;

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


    public static int SAMPLE_SIZE = 6;
    public static int REQUIRED_SIZE = 75;

    private static String myImageUrl = null;

    private static String picCacheUrl = null;

    private static Picasso picasso = Picasso.get();


    public static final boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static final void loadImage(String picUrl, ImageView target){
//        if(picasso != null){
//            if(picCacheUrl != null && picCacheUrl.equals(picUrl)){
//                picasso.load(picCacheUrl).into(target);
//            } else {
//                picasso.invalidate(picCacheUrl);
//                picCacheUrl = picUrl;
//                picasso.load(picCacheUrl).memoryPolicy(MemoryPolicy.NO_CACHE).into(target);
//            }
//        } else {
//            picCacheUrl = picUrl;
//            picasso = Picasso.get();
//            picasso.load(picCacheUrl).memoryPolicy(MemoryPolicy.NO_CACHE).into(target);
//        }
        picasso.load(picCacheUrl).memoryPolicy(MemoryPolicy.NO_CACHE).into(target);

    }

    public static final Bitmap reduceImageSize(Context context, Uri imageUri){
            Bitmap bitmap = decodeSampledBitmapFromResource(imageUri.getPath().toString(), 150, 150);
            Log.d("-----", "-------\n\nImage size -> " + bitmap.getByteCount() + "\n\n-------------------");
            return bitmap;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String imageUrl,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imageUrl, options);
//        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imageUrl, options);
    }
}
