package com.vgeekers.neeraqua;

import android.content.SharedPreferences;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.iid.FirebaseInstanceId;

import static com.vgeekers.neeraqua.TerminalConstant.MY_PREFS;
import static com.vgeekers.neeraqua.TerminalConstant.USER_LOGIN_DONE_KEY;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView view = findViewById(R.id.imageView);
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
        }, 2500);
        System.out.println(FirebaseInstanceId.getInstance().getToken());
    }
}
