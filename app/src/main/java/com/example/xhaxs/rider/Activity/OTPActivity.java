package com.example.xhaxs.rider.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.xhaxs.rider.AppUtils;
import com.example.xhaxs.rider.Datatype.UserSumData;
import com.example.xhaxs.rider.LogHandle;
import com.example.xhaxs.rider.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {
    private static final String TAG = "Phone Authentication ";
    private String verificationid;
    private FirebaseAuth mAuth;
    private ProgressBar verifyProgressBar;
    private EditText editText;
    private String phoneNumberFinal;
    private String countryCodeFinal;
    private String phonenumber;

    private UserSumData userSumData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

//        Log.d("--------------OTP", mCurrentUser.getUid());

        userSumData = getIntent().getParcelableExtra(AppUtils.CURRENT_USER_STRING);

        mAuth = FirebaseAuth.getInstance();
        verifyProgressBar = findViewById(R.id.verifyProgressBar);
        editText = findViewById(R.id.editTextCode);

        phoneNumberFinal = getIntent().getStringExtra("phoneNumberFinal");
        countryCodeFinal = getIntent().getStringExtra("countryCodeFinal");

        phonenumber = countryCodeFinal + phoneNumberFinal;

        sendVerificationCode(phonenumber);

        findViewById(R.id.buttonVerify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString().trim();

                if ((code.isEmpty() || code.length() < 6)){

                    editText.setError("Enter valid code...");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
    }
    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.updatePhoneNumber(credential);
//        signInWithCredential(credential);
        uploadToDatabase();
    }

    private void uploadToDatabase(){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users/" + userSumData.getUid());
        HashMap<String, Object> map = new HashMap<>();
        map.put(AppUtils.PHONE_VERIFIED_STRING, true);
        map.put(AppUtils.PHONE_NUMBER_STRING, phoneNumberFinal);
        map.put(AppUtils.COUNTRY_CODE_STRING, countryCodeFinal);

        db.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    verifyProgressBar.setVisibility(View.INVISIBLE);

                    LogHandle.flushCache();

                    Intent intent = new Intent(OTPActivity.this, SearchRideActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(OTPActivity.this, "Error Updating", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(OTPActivity.this, PhoneNumberActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

//                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users/" + userSumData.getUid());
//                            HashMap<String, Object> map = new HashMap<>();
//                            map.put(AppUtils.PHONE_VERIFIED_STRING, true);
//                            map.put(AppUtils.PHONE_NUMBER_STRING, phoneNumberFinal);
//                            map.put(AppUtils.COUNTRY_CODE_STRING, countryCodeFinal);
//
//                            db.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if(task.isSuccessful()){
//                                        verifyProgressBar.setVisibility(View.INVISIBLE);
//
//                                        LogHandle.flushCache();
//
//                                        Intent intent = new Intent(OTPActivity.this, SearchRideActivity.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                        startActivity(intent);
//                                    } else {
//                                        Toast.makeText(OTPActivity.this, "Error Updating", Toast.LENGTH_LONG).show();
//                                        Intent intent = new Intent(OTPActivity.this, PhoneNumberActivity.class);
//                                        startActivity(intent);
//                                        finish();
//
//                                    }
//                                }
//                            });

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(OTPActivity.this, "Verification Code Invalid",
                                        Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(OTPActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void sendVerificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                120,
                TimeUnit.SECONDS,
                this,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken)
        {
            super.onCodeSent(verificationId, forceResendingToken);
            verificationid = verificationId;
            Log.d(TAG, "onCodeSent:" + verificationId);
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                verifyProgressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
            Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.w(TAG, "onVerificationFailed", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
                Toast.makeText(OTPActivity.this, "Invalid Request",Toast.LENGTH_SHORT).show();
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
                Toast.makeText(OTPActivity.this, "OTP Limit Exceeded",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(OTPActivity.this, PhoneNumberActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    };
}