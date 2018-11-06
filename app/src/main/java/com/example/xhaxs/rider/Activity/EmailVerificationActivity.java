package com.example.xhaxs.rider.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.xhaxs.rider.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class EmailVerificationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText verifyingemail;
    private Button requestforverfiybutton;
    private ProgressBar verifyemailprogressbar;

    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        verifyingemail = findViewById(R.id.YourEmail);
        requestforverfiybutton = findViewById(R.id.VerifyEmail);
        verifyemailprogressbar = findViewById(R.id.VerifyProgressBar);
        mAuth = FirebaseAuth.getInstance();

        verifyingemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value = s.toString();
                if (TextUtils.isEmpty(value) == false) {
                    email = value;
                } else {
                    verifyingemail.setError("Enter valid email");
                    verifyingemail.requestFocus();
                    email=null;
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        requestforverfiybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(email)) {
                    verifyemailprogressbar.setVisibility(View.VISIBLE);
                    if(mAuth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(EmailVerificationActivity.this, "Email already verified!", Toast.LENGTH_LONG).show();
                    } else if (!mAuth.getCurrentUser().isEmailVerified() && email.equals(mAuth.getCurrentUser().getEmail())){
                        mAuth.getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        verifyemailprogressbar.setVisibility(View.INVISIBLE);
                                        if (task.isSuccessful()) {
                                            Toast.makeText(EmailVerificationActivity.this, "Verificaion email sent to : " + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                                            Intent sendtomain = new Intent(EmailVerificationActivity.this, LoginActivity.class);
                                            startActivity(sendtomain);
                                            finish();
                                        } else{
                                            Toast.makeText(EmailVerificationActivity.this,"Failed to sent verification email",Toast.LENGTH_SHORT).show();
                                        }
                                        Intent sendtomain = new Intent(EmailVerificationActivity.this, LoginActivity.class);
                                        startActivity(sendtomain);
                                        finish();
                                    }
                                });
                    } else{
                        verifyemailprogressbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(EmailVerificationActivity.this,"Please, your Email",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(EmailVerificationActivity.this, "Please provide email for reset request", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
