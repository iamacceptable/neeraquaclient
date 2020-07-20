package com.neeraqua.neerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.material.textfield.TextInputLayout;
import com.neeraqua.neerapp.response.CommonResponse;
import com.neeraqua.neerapp.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    private TextInputLayout forgotPasswordMobile;
    private TextInputLayout forgotPasswordNewPassword;
    private TextInputLayout forgotPasswordConfirmPassword;
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
        setContentView(R.layout.activity_forgot_password);
        forgotPasswordConfirmPassword = findViewById(R.id.forgotPasswrodConfirmPassword);
        forgotPasswordNewPassword = findViewById(R.id.forgotPasswordNewPassword);
        forgotPasswordMobile = findViewById(R.id.forgotPasswordMobile);
        Button sendOtp = findViewById(R.id.sendOtpBtn);
        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validMobile() || !validPassword() || !validConfirmPassword()) {
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        passwordResetServerCall();
                    }
                }).start();
            }
        });
    }

    private void passwordResetServerCall() {
        showProgress();
        String mobile = forgotPasswordMobile.getEditText().getText().toString();
        String password = forgotPasswordNewPassword.getEditText().getText().toString();
        RetrofitApi.getPaniServicesObject().getForgotPasswordResponse(mobile, password).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = response.body();
                    if (commonResponse != null && commonResponse.getErrorCode().equals(TerminalConstant.SUCCESS)) {
                        Toast.makeText(ForgotPassword.this, commonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPassword.this, Authentication.class));
                    } else if (commonResponse != null) {
                        Toast.makeText(ForgotPassword.this, commonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                stopProgress();
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                stopProgress();
                Toast.makeText(ForgotPassword.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validPassword() {
        String password = forgotPasswordMobile.getEditText().getText().toString();
        if (password.length() == 0) {
            forgotPasswordNewPassword.setError("Password can't be empty!!");
            return false;
        } else if (password.length() < 8) {
            forgotPasswordNewPassword.setError("Password too short!!");
            return false;
        } else if (password.length() > 12) {
            forgotPasswordNewPassword.setError("Password too long!!");
            return false;
        }
        forgotPasswordNewPassword.setError(null);
        return true;
    }

    private boolean validMobile() {
        String mobile = forgotPasswordMobile.getEditText().getText().toString();
        if (mobile.length() == 0) {
            forgotPasswordMobile.setError("Mobile Number can't be empty!!");
            return false;
        } else if (mobile.length() < 10) {
            forgotPasswordMobile.setError("Mobile Number too short!!");
            return false;
        } else if (mobile.length() > 10) {
            forgotPasswordMobile.setError("Mobile Number too long!!");
            return false;
        }
        forgotPasswordMobile.setError(null);
        return true;
    }

    private boolean validConfirmPassword() {
        String password = forgotPasswordConfirmPassword.getEditText().getText().toString();
        if (password.length() == 0) {
            forgotPasswordConfirmPassword.setError("Password can't be empty!!");
            return false;
        } else if (password.length() < 8) {
            forgotPasswordConfirmPassword.setError("Password too short!!");
            return false;
        } else if (password.length() > 12) {
            forgotPasswordConfirmPassword.setError("Password too long!!");
            return false;
        }
        forgotPasswordConfirmPassword.setError(null);
        return true;
    }

    private static final int APP_REQUEST_CODE = 99;

    private void sendFacebookOtpForVerification() {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
            new AccountKitConfiguration.AccountKitConfigurationBuilder(
                LoginType.PHONE,
                AccountKitActivity.ResponseType.CODE); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
            AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
            configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                } else {
                    toastMessage = String.format("Success:%s...", loginResult.getAuthorizationCode().substring(0, 10));
                }
                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.
                // Success! Start your next activity...
                //goToMyLoggedInActivity();
            }
            // Surface the result to your user in an appropriate way.
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
            passwordResetServerCall();
        }
    }
}
