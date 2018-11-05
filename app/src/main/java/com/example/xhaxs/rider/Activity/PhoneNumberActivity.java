package com.example.xhaxs.rider.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xhaxs.rider.R;

public class PhoneNumberActivity extends AppCompatActivity {

    private TextView mCountryCode;
    private EditText mPhoneNumber;

    private String countryCodeFinal;
    private String phoneNumberFinal;

    private String phonenumber;

    private Button mSendOTPButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        mCountryCode = findViewById(R.id.et_sumit_profile_country_code);
        countryCodeFinal = mCountryCode.getText().toString();
        mPhoneNumber = findViewById(R.id.et_sumit_profile_phone);
        mSendOTPButton = findViewById(R.id.b_apn_send_otp);

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
                    mPhoneNumber.setError("Enter valid number");
                    mPhoneNumber.requestFocus();
                    phoneNumberFinal = null;
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSendOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(phoneNumberFinal)
                        && !TextUtils.isEmpty(countryCodeFinal)){
                    phonenumber = countryCodeFinal + phoneNumberFinal;
                    Intent intent = new Intent(PhoneNumberActivity.this, OTPActivity.class);
                    intent.putExtra("phoneNumberFinal", phoneNumberFinal);
                    intent.putExtra("countryCodeFinal", countryCodeFinal);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
