package com.vgeekers.neeraqua;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Otp extends AppCompatActivity {
    EditText otp;
    Button verifyOtp;
    TextView resendOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        otp = findViewById(R.id.otp);
        verifyOtp = findViewById(R.id.verifyOtp);
        resendOTP = findViewById(R.id.resendOTP);
        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Otp.this, Authentication.class));
            }
        });
    }
}