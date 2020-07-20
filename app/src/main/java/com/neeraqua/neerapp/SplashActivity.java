package com.neeraqua.neerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import static com.neeraqua.neerapp.TerminalConstant.MY_PREFS;
import static com.neeraqua.neerapp.TerminalConstant.USER_LOGIN_DONE_KEY;

public class SplashActivity extends AppCompatActivity {

    private static final long ANIMATION_DURATION = 2200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final ImageView view = findViewById(R.id.imageView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
                boolean isLoginDone = prefs.getBoolean(USER_LOGIN_DONE_KEY, false);
                if (isLoginDone) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, Authentication.class));
                }
                finish();
            }
        }, 1500);
    }



}


