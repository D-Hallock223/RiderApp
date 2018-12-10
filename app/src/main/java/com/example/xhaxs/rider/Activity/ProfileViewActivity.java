package com.example.xhaxs.rider.Activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Network;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xhaxs.rider.AppUtils;
import com.example.xhaxs.rider.Datatype.CreateRideDetailData;
import com.example.xhaxs.rider.Datatype.UserSumData;
import com.example.xhaxs.rider.LogHandle;
import com.example.xhaxs.rider.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//import com.squareup.picasso.MemoryPolicy;
//import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import bolts.Bolts;

public class ProfileViewActivity extends AppCompatActivity {

    public static final String PROFILER_STRING = "Profiler";

    private FirebaseUser mCurrentUser;
    private UserSumData toShowUser;

    private ImageView mProfilePic;
    private TextView mUserName;
    private TextView mEmail;
    private TextView mContact;
    private TextView mTotalRides;
    private TextView mGender;

    private LinearLayout mEditValue;

    private ImageButton mBEditEmail;

    private ImageButton mBEditPhoneContact;
    private EditText mEditTextNewValue;
    private ImageButton mChooseNewValue;
    private ImageButton mCancelNewValue;
    private String newValue;

    private String downloadUrl;

    private ImageButton mImageChangePic;

    private Boolean editable;

    private Uri selectImageUri;

    private ProgressDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();
        mCurrentUser = LogHandle.checkLogin(FirebaseAuth.getInstance(), this);
        LogHandle.checkDetailsAdded(mCurrentUser, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentUser = LogHandle.checkLogin(FirebaseAuth.getInstance(), this);
        LogHandle.checkDetailsAdded(mCurrentUser, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_profile_view);

        editable = false;

        mCurrentUser = LogHandle.checkLogin(FirebaseAuth.getInstance(), this);
        LogHandle.checkDetailsAdded(mCurrentUser, this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setElevation(0);

        mCurrentUser = LogHandle.checkLogin(FirebaseAuth.getInstance(), this);
        LogHandle.checkDetailsAdded(mCurrentUser, this);

        Log.d("--------------PROFILE", mCurrentUser.getUid());

        mProfilePic = findViewById(R.id.im_apv_profile_pic);
        mUserName = findViewById(R.id.tv_apv_u_name);
        mEmail = findViewById(R.id.tv_apv_email);
        mContact = findViewById(R.id.tv_apv_contacts);
        mTotalRides = findViewById(R.id.tv_apv_total_rides);
        mGender = findViewById(R.id.tv_apv_gender);
        mEditValue = findViewById(R.id.ll_apv_edit_layout);
        mBEditEmail = findViewById(R.id.ib_email_edit);
        mBEditPhoneContact = findViewById(R.id.ib_contact_edit);
        mEditTextNewValue = findViewById(R.id.et_apv_new_value);
        mChooseNewValue = findViewById(R.id.ib_apv_edit_true);
        mCancelNewValue = findViewById(R.id.ib_apv_edit_false);
        mImageChangePic = findViewById(R.id.ib_change_profile_image);

        mEditValue.setVisibility(View.GONE);
        mBEditEmail.setVisibility(View.GONE);
        mImageChangePic.setVisibility(View.GONE);
        mBEditPhoneContact.setVisibility(View.GONE);


        mImageChangePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                if (intent.resolveActivity(getPackageManager()) != null) {
//                    startActivityForResult(intent, ProfileDetailsActivity.GALLERY_PICK);
//                }
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(ProfileViewActivity.this);
            }
        });

        if(getIntent().hasExtra(PROFILER_STRING)){
            toShowUser = getIntent().getParcelableExtra(PROFILER_STRING);
        } else  {
            Toast.makeText(this, "No Riders Found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadLocalDetails();

        if(!toShowUser.getUid().equals(mCurrentUser.getUid())){
            (findViewById(R.id.ll_apv_email)).setVisibility(View.GONE);
            (findViewById(R.id.ll_apv_contact)).setVisibility(View.GONE);

//            mBEditEmail.setVisibility(View.GONE);
//            mBEditPhoneContact.setVisibility(View.GONE);
            loadUserDetails();
        } else {

//            editable = true;
//            mBEditEmail.setVisibility(View.VISIBLE);
//            mBEditPhoneContact.setVisibility(View.VISIBLE);
            mImageChangePic.setVisibility(View.VISIBLE);

            Map<String, Object> map = LogHandle.mapCache;

            if(map != null){
                Log.d("sdfsdfoksfd---", map.toString());
                loadNetDetails(map);
            } else {
                loadUserDetails();
            }
        }

        loadTotalRides();
    }

    public void updateDatabase(Uri photoUri, int imageSize){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> map = new HashMap<>();
        map.put(AppUtils.PROFILE_PIC_URL_STRING, photoUri.toString());
        map.put(AppUtils.PROFILE_PIC_SIZE_STRING, imageSize);
        db.child("Users/" + mCurrentUser.getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    LogHandle.flushCache();
                    Toast.makeText(ProfileViewActivity.this, "Profile Picture Uploaded!", Toast.LENGTH_LONG).show();
                } else  {
                    Toast.makeText(ProfileViewActivity.this, "Error Uploading Picture", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loadUserDetails(){
        if(AppUtils.isNetworkAvailable(this) == false){
            Toast.makeText(this, "Network Not Available", Toast.LENGTH_SHORT).show();
            mContact.setText("-");
            return;
        } else {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            db.child("Users").child(toShowUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            Log.d("-0-0-", "fetching for -> " + toShowUser.getUid());
                            if(map == null){
                                mContact.setText("-");
                                mGender.setText("-");
                                return;
                            } else {
                                Log.d("909090909", "-0-0-0-0-0-0-0-0-0-9230-4-" + map.toString());

                                loadNetDetails(map);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void loadTotalRides(){
        if(AppUtils.isNetworkAvailable(this) == false){
            Toast.makeText(this, "Network Not Available", Toast.LENGTH_SHORT).show();
            mTotalRides.setText("-");
            return;
        } else {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            db.child("Riders").orderByChild(CreateRideDetailData.RIDE_USER_ARRAY_STRING + "/"
                    + toShowUser.getUid() + "/"
                    + UserSumData.UID_STRING)
                    .equalTo(toShowUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            if(map == null){
                                mTotalRides.setText("0");
                                return;
                            } else {
                                mTotalRides.setText(Integer.toString(map.size()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadLocalDetails(){
        mUserName.setText(toShowUser.getUname());
        mEmail.setText(toShowUser.getEmail());

    }
    private void loadNetDetails(Map<String, Object> map){

        String cv = "";
        String cc = "+91";
        String gender = "-";

        if(map.get("phoneNumber") != null){
            cv = map.get("phoneNumber").toString();
        }
        if(map.get("countryCode") != null){
            cc = map.get("countryCode").toString();
        }

        if(map.get("gender") != null){
            gender = ProfileDetailsActivity.AVAILABLE_GENDERS[Integer.parseInt(map.get("gender").toString())];
        }

        if(map.get(AppUtils.PROFILE_PIC_URL_STRING) != null && map.get(AppUtils.PROFILE_PIC_SIZE_STRING) != null){
            Log.d("----\n\n", "\n\n\n" + "\t----Loading data----\n\n\n");
//            AppUtils.loadImage(map.get(AppUtils.PROFILE_PIC_URL_STRING).toString(), mProfilePic);
//            Picasso.get().load(map.get(AppUtils.PROFILE_PIC_URL_STRING).toString()).memoryPolicy(MemoryPolicy.NO_CACHE).into(mProfilePic);
              AppUtils.loadFromNetImage(ProfileViewActivity.this,
                      map.get(AppUtils.PROFILE_PIC_URL_STRING).toString(),
                      mProfilePic,
                      Integer.parseInt(map.get(AppUtils.PROFILE_PIC_SIZE_STRING).toString()));
        }

        mContact.setText(cc + " " + cv);
        mGender.setText(gender);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ProfileDetailsActivity.GALLERY_PICK && resultCode == RESULT_OK) {
            selectImageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){

                selectImageUri = result.getUri();

                final Bitmap imageBitmap = AppUtils.reduceImageSize(ProfileViewActivity.this, selectImageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                final int imageSize = imageBitmap.getByteCount();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                final byte[] uploadImageAsByteArray = baos.toByteArray();

                mProfilePic.setImageBitmap(imageBitmap);

//                String imageExt;

                if(selectImageUri != null){
//                    ContentResolver cr = getContentResolver();
//                    MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//                    imageExt = mimeTypeMap.getExtensionFromMimeType(cr.getType(selectImageUri));

                    final StorageReference mImageRef = FirebaseStorage.getInstance().getReference().child(AppUtils.PROFILE_IMAGE_FOLDER_STRING);

                    final StorageReference mStorageRef = mImageRef.child(mCurrentUser.getUid() + "_" + UUID.randomUUID().toString() + ".jpeg");

                    UploadTask uploadTask = mStorageRef.putBytes(uploadImageAsByteArray);

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){
                                throw task.getException();
                            }
                            return mStorageRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ProfileViewActivity.this,"Profile Image Updated", Toast.LENGTH_LONG).show();
                                Uri uri = task.getResult();

                                Log.d("-------", "++++++++++\n\t" + uri.toString() + "------------\n\t");

                                updateDatabase(uri, imageSize);
                            }
                        }
                    });
//                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//
//                            if(task.isSuccessful()){
//                                Toast.makeText(ProfileViewActivity.this, "Profile Image Stored Successfully", Toast.LENGTH_SHORT).show();
//
//                                downloadUrl = mStorageRef.getDownloadUrl().toString();
//
//                                Uri uri = Uri.parse(downloadUrl);
//
//                                Log.d("-------", "++++++++++\n\t" + downloadUrl + "------------\n\t");
//
//                                updateDatabase(uri);
//                            }
//                        }
//                    });

                }

            } else  {
                Toast.makeText(ProfileViewActivity.this, "Error cropping", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
