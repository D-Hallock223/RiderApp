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

public class ResetPasswordActivity extends AppCompatActivity {

    private Button resetpasswordbutton;
    private EditText resetemail;
    private FirebaseAuth mFirebaseAuth;
    private ProgressBar resetprogressbar;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mFirebaseAuth = FirebaseAuth.getInstance();
        resetprogressbar = findViewById(R.id.Resetprogressbar);
        resetemail = findViewById(R.id.Resetemail);
        resetpasswordbutton = findViewById(R.id.Resetpassword);

        resetemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value = s.toString();
                if (TextUtils.isEmpty(value) == false) {
                    email = value;
                } else {
                    resetemail.setError("Enter valid email");
                    resetemail.requestFocus();
                    email=null;
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        resetpasswordbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(email)) {
                    resetprogressbar.setVisibility(View.VISIBLE);
                    mFirebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            resetprogressbar.setVisibility(View.INVISIBLE);
                            if(task.isSuccessful()){
                                Toast.makeText(ResetPasswordActivity.this,"Password reset email sent",Toast.LENGTH_LONG).show();
                                Intent sendtomain = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                startActivity(sendtomain);
                                finish();
                            } else {
                                Toast.makeText(ResetPasswordActivity.this,task.getException().toString(),Toast.LENGTH_LONG).show();
                                Intent sendtoreg = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                startActivity(sendtoreg);
                                finish();
                            }
                        }
                    });
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Please provide email for reset request", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
