package com.example.xhaxs.rider.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.xhaxs.rider.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileDetailsActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private CircleImageView mProfilePic;
    private EditText mUserName;
    private EditText mCountryCode;
    private EditText mPhoneNumber;
    private Button mSubmitDetails;

    private Uri selectImageUri;
    private String userNameFinal;
    private String countryCodeFinal;
    private String phoneNumberFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        mProfilePic = findViewById(R.id.iv_submit_profile_pic);
        mUserName = findViewById(R.id.et_sumit_profile_name);
//        mCountryCode = findViewById(R.id.et_sumit_profile_country_code);
        mPhoneNumber = findViewById(R.id.et_sumit_profile_phone);
        mSubmitDetails = findViewById(R.id.b_submit_profile_od);

        mProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        mUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value = s.toString();
                if (TextUtils.isEmpty(value) == false) {
                    userNameFinal = value;
                } else {
                    userNameFinal = "";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value = s.toString();
                if (TextUtils.isEmpty(value) == false && value.length() == 10) {
                    phoneNumberFinal = value;
                } else {
                    phoneNumberFinal = "";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSubmitDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * TODO
                 * 1. Implement the onClickListener for submitting profile details.
                 * 2. Check the details verification for phone contact, country Code.
                 */
                Intent intent = new Intent(ProfileDetailsActivity.this, SearchRideActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            selectImageUri = data.getData();
            if (null != selectImageUri) {
                String path = getPathFromURI(selectImageUri);
                mProfilePic.setImageURI(selectImageUri);
            }
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}
