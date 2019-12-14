package com.vgeekers.neeraqua;

import android.os.Handler;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.vgeekers.neeraqua.adapters.AuthPageAdapter;

public class Authentication extends AppCompatActivity {

    TabLayout authTab;
    ViewPager authViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        authTab = findViewById(R.id.authTab);
        authViewPager = findViewById(R.id.authViewPager);
        AuthPageAdapter authPageAdapter = new AuthPageAdapter(getSupportFragmentManager());
        authViewPager.setAdapter(authPageAdapter);
        authTab.setupWithViewPager(authViewPager);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
