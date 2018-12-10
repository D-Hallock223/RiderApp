package com.example.xhaxs.rider.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xhaxs.rider.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    private TextView textView;
    private ImageView imageView;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_splash_screen);

        textView = (TextView) findViewById(R.id.welcome);
        imageView = (ImageView) findViewById(R.id.HillShare);
        mAuth = FirebaseAuth.getInstance();

        Animation mAnimation = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        textView.startAnimation(mAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mAuth.getCurrentUser() != null){
                    startActivity(new Intent(SplashScreen.this, SearchRideActivity.class));
                } else {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    finish();
                }
            }
        },SPLASH_TIME_OUT);

    }
}
