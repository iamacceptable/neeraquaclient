package com.neeraqua.neerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.neeraqua.neerapp.response.CommonResponse;
import com.neeraqua.neerapp.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpOtpActivity extends AppCompatActivity {

    private EditText otpEditText;
    private Button verifyOtp;
    private TextView resendOTP;
    private String mMobileNumber = "";
    private String mName = "";
    private String mPassword = "";
    private String mHouseNumber = "";
    private String mState = "";
    private String mCity = "";
    private String mLocality = "";
    private String mPlainOtpStr = "";
    private ProgressDialog mProgressDialog;

    private void showProgress() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Processing");
        mProgressDialog.setMessage("Please Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
    }

    private void stopProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        otpEditText = findViewById(R.id.otp);
        verifyOtp = findViewById(R.id.verifyOtp);
        resendOTP = findViewById(R.id.resendOTP);
        Intent intent = getIntent();
        mMobileNumber = intent.getStringExtra("mobile");
        mName = intent.getStringExtra("name");
        mPassword = intent.getStringExtra("pass");
        mHouseNumber = intent.getStringExtra("hno");
        mState = intent.getStringExtra("state");
        mCity = intent.getStringExtra("city");
        mLocality = intent.getStringExtra("locality");
        initiateOtp();
        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initiateOtp();
            }
        });
        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otpInput = otpEditText.getText().toString();
                if (otpInput.isEmpty()) {
                    otpEditText.setError("This field is mandatory");
                    otpEditText.requestFocus();
                    return;
                }
                if (otpInput.equalsIgnoreCase(mPlainOtpStr)) {
                    Toast.makeText(getApplicationContext(), "OTP matched successfully", Toast.LENGTH_SHORT).show();
                    getSignUpApiCall();
                } else {
                    Toast.makeText(getApplicationContext(), "OTP mismatched", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initiateOtp() {
        showProgress();
        RetrofitApi.getPaniServicesObject().initiateOtp(mMobileNumber).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = response.body();
                    if (commonResponse != null && commonResponse.getErrorCode().equals(TerminalConstant.SUCCESS)) {
                        Toast.makeText(getApplicationContext(), "OTP send successfully", Toast.LENGTH_SHORT).show();
                        mPlainOtpStr = commonResponse.getErrorMessage();
                    } else if (commonResponse != null) {
                        Toast.makeText(getApplicationContext(), commonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                } else if (response.code() == 404) {
                    Toast.makeText(getApplicationContext(), "No SMS Gateway integrated yet", Toast.LENGTH_SHORT).show();
                }
                stopProgress();
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                stopProgress();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSignUpApiCall() {
        String token = FirebaseInstanceId.getInstance().getToken();
        showProgress();
        RetrofitApi.getPaniServicesObject().getSignUpResponse(mName, mMobileNumber, mPassword, mHouseNumber, mState, mCity, mLocality, token).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = response.body();
                    if (commonResponse != null && commonResponse.getErrorCode().equals(TerminalConstant.SUCCESS)) {
                        Toast.makeText(getApplicationContext(), "SignUp Successful, please login again to confirm your credentials", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Authentication.class));
                    } else if (commonResponse != null && commonResponse.getErrorCode() != null) {
                        Toast.makeText(getApplicationContext(), commonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                stopProgress();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}