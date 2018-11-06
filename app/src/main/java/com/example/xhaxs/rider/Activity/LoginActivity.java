package com.example.xhaxs.rider.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.xhaxs.rider.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "facelog";
    private Button loginbutton, loginregbutton,resetpasswordbutton;
    private EditText loginemail, loginpassword;
    private ProgressBar loginprogressbar;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginbutton = findViewById(R.id.loginbutton);
        loginregbutton = findViewById(R.id.loginregbutton);
        loginemail = findViewById(R.id.loginemail);
        loginpassword = findViewById(R.id.loginpassword);
        loginprogressbar = findViewById(R.id.loginprogressbar);
        loginregbutton = findViewById(R.id.loginregbutton);
        resetpasswordbutton = findViewById(R.id.b_reset_password);

        mAuth = FirebaseAuth.getInstance();


        loginregbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendtoreg = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(sendtoreg);
                finish();

            }
        });
        resetpasswordbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendtoreg = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(sendtoreg);
                finish();
            }
        });
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email = loginemail.getText().toString();
                String password = loginpassword.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    loginprogressbar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                if(mAuth.getCurrentUser().isEmailVerified()) {
//                                    Intent sendtomain = new Intent(LoginActivity.this, SearchRideActivity.class);
//                                    startActivity(sendtomain);
//                                    finish();
                                } else {
//                                    Toast.makeText(LoginActivity.this,"Please verify your email",Toast.LENGTH_LONG).show();
//                                    Intent sendtomain = new Intent(LoginActivity.this, EmailVerificationActivity.class);
//                                    startActivity(sendtomain);
//                                    finish();
                                }
                                Intent sendtomain = new Intent(LoginActivity.this, SearchRideActivity.class);
                                startActivity(sendtomain);
                                finish();
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error:" + error, Toast.LENGTH_LONG).show();
                            }
                            loginprogressbar.setVisibility(View.INVISIBLE);
                        }
                    });

                } else {
                    Toast.makeText(LoginActivity.this, "Fill the form completely!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.fbloginbutton);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {


            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                loginprogressbar.setVisibility(View.VISIBLE);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentuser = mAuth.getCurrentUser();
        if (currentuser != null) {
            // user is logged in
            Intent sendtomain = new Intent(LoginActivity.this, SearchRideActivity.class);
            startActivity(sendtomain);
            finish();
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Log.d(TAG, "signInWithCredential:success");
                            // FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            Intent sendtomain = new Intent(LoginActivity.this, SearchRideActivity.class);
                            startActivity(sendtomain);
                            loginprogressbar.setVisibility(View.INVISIBLE);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            String error = task.getException().getMessage();
                            Toast.makeText(LoginActivity.this, "Error:" + error,
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });
    }

    public void close(){
        finish();
    }

}