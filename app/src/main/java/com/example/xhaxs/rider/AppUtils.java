package com.example.xhaxs.rider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;

import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
    public static final String PROFILE_PIC_SIZE_STRING = "profilePicSize";
    public static final String APP_MAIN_NAME = "hillshare";
    public static final String APP_IMAGE_PATH = "images";

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final int MEDIA_TYPE_IMAGE = 1;

    public static int SAMPLE_SIZE = 6;
    public static int REQUIRED_SIZE = 75;

    private static String myImageUrl = null;

    private static String picCacheUrl = null;

//    private static Picasso picasso = Picasso.get();


    public static final boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
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

    public static void loadFromNetImage(final Activity activity, final String path, final ImageView imageView, final int maxBytes){

//        getStoragePermission(activity);
        Uri pathUri = checkInCache(path, MEDIA_TYPE_IMAGE);
        if(pathUri != null){
            imageView.setImageURI(pathUri);
            return;
        }

        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(path);

        reference.getBytes(maxBytes).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
                cacheFile(bytes, MEDIA_TYPE_IMAGE, path);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity.getApplicationContext(), "Error Loading Image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static final void getStoragePermission(Activity activity){
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    public static final void cacheFile(final byte[] bytes, final int type, final String path){


        AsyncTask<Void, Void, Void> as = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                String storeagePath = APP_MAIN_NAME;
                if(type == MEDIA_TYPE_IMAGE){
                    storeagePath = storeagePath + File.separator + APP_IMAGE_PATH;
                }

                File folder = new File(Environment.getExternalStorageDirectory().toString(), storeagePath);
                if(folder.exists() == false){
                    if(folder.mkdirs() == false) {
                        Log.d("\n\n--", "\n\n-------------\nError creating folders" + "\n\n");
                        return null;
                    }
                }

                String fileName = URLUtil.guessFileName(path, null, null);

                Log.d("----", "-----------------------\n\n\tGot Guessed File Name: " + fileName + "\n\n\n---------------------");

                File file = new File(Environment.getExternalStorageDirectory().toString(), storeagePath + File.separator + fileName);
                try {
                    FileOutputStream fo = new FileOutputStream(file);
                    fo.write(bytes);
                    fo.close();
                    Log.d("----", "-----------------------\n\tFile Cached in -> " + file + "\n---------------------");
                } catch (IOException e){
                    Log.d("----", "-----------------------\n\tError Creating File" + e.getMessage() + "\n---------------------");
                }
                return null;
            }
        };
        as.execute();
    }

    public static final Uri checkInCache(String path, int type){
        String storeagePath = APP_MAIN_NAME;
        if(type == MEDIA_TYPE_IMAGE){
            storeagePath = storeagePath + File.separator + APP_IMAGE_PATH;
        }
        String guessedName = URLUtil.guessFileName(path, null, null);
        Log.d("----", "-----------------------\n\n\tGot Guessed File Name: " + guessedName + "\n\n\n---------------------");
        String filePath = storeagePath + File.separator + guessedName;
//        String filePath = Environment.getExternalStorageDirectory() + File.separator + guessedName;
        File file = new File(Environment.getExternalStorageDirectory().toString(), filePath);
        Log.d("----", "-----------------------\n\n\tChecking File in -> " + file + "\n\n\n---------------------");
        if(file.exists() == true){
            Log.d("----", "-----------------------\n\n\tFile Found in Cache\n\n\n---------------------");
            return Uri.fromFile(file);
        }
        return null;
    }
}

//Cached in -> /storage/emulated/0/hillshare:images/lbojf2AHrlSgfnfnt844hN6ULH82_3c757486-b45a-44d4-9319-a69d2fd48b51.jpeg
//Checking File in -> /storage/emulated/0/hillshare:images:lbojf2AHrlSgfnfnt844hN6ULH82_3c757486-b45a-44d4-9319-a69d2fd48b51.jpeg
