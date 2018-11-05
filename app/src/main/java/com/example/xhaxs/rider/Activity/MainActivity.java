package com.example.xhaxs.rider.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.xhaxs.rider.LogHandle;
import com.example.xhaxs.rider.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Toolbar maintoolbar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        maintoolbar = findViewById(R.id.maintoolbar);
        setSupportActionBar(maintoolbar);
        getSupportActionBar().setTitle("HillShare");

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();

        LogHandle.checkLogin(FirebaseAuth.getInstance(), MainActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_logout_btn:
                LogHandle.logout(FirebaseAuth.getInstance(), this);
                return true;

            default:
                return false;

        }
    }

    private void logout() {

        mAuth.signOut();
        LoginManager.getInstance().logOut();
        Intent sendtologin = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(sendtologin);
        finish();

    }

    public void close(){
        finish();
    }
}
