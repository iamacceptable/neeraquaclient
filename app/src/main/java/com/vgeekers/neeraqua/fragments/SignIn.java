package com.vgeekers.neeraqua.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.vgeekers.neeraqua.ForgotPassword;
import com.vgeekers.neeraqua.MainActivity;
import com.vgeekers.neeraqua.R;
import com.vgeekers.neeraqua.TerminalConstant;
import com.vgeekers.neeraqua.response.LoginResponse;
import com.vgeekers.neeraqua.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.vgeekers.neeraqua.TerminalConstant.MY_PREFS;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignIn extends BaseFragment {

    private TextView authForgotPassword;
    private TextInputLayout authSignInMobile;
    private TextInputLayout authSignInPassword;
    private Button authSignInBtn;

    public SignIn() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        authForgotPassword = v.findViewById(R.id.authForgotPassword);
        authSignInBtn = v.findViewById(R.id.authSignInBtn);
        authSignInMobile = v.findViewById(R.id.authSignInMobile);
        authSignInPassword = v.findViewById(R.id.authSignInPassword);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ForgotPassword.class));
            }
        });
        authSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validMobile() || !validPassword()) {
                    return;
                }
                doLogin();//
            }
        });
    }

    private void doLogin() {
        showProgress();
        String mobile = authSignInMobile.getEditText().getText().toString().trim();
        String password = authSignInPassword.getEditText().getText().toString().trim();
        String token = FirebaseInstanceId.getInstance().getToken();
        RetrofitApi.getPaniServicesObject().getLoginResponse(mobile, password, token).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    LoginResponse commonResponse = response.body();
                    if (commonResponse != null) {
                        if (commonResponse.getErrorCode() != null && commonResponse.getErrorCode().equals(TerminalConstant.SUCCESS) && commonResponse.getIsAllowLogin()) {
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
                            editor.putBoolean(TerminalConstant.USER_LOGIN_DONE_KEY, true);
                            editor.putString(TerminalConstant.USER_ID_KEY, commonResponse.getUserId());
                            editor.apply();
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                        } else {
                            Toast.makeText(getContext(), commonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                stopProgress();
            }
        });
    }

    private boolean validPassword() {
        String password = authSignInPassword.getEditText().getText().toString();
        if (password.length() == 0) {
            authSignInPassword.setError("Password can't be empty!!");
            return false;
        } else if (password.length() < 3) {
            authSignInPassword.setError("Password too short!!");
            return false;
        } else if (password.length() > 12) {
            authSignInPassword.setError("Password too long!!");
            return false;
        }
        authSignInMobile.setError(null);
        return true;
    }

    private boolean validMobile() {
        String mobile = authSignInMobile.getEditText().getText().toString();
        if (mobile.length() == 0) {
            authSignInMobile.setError("Mobile Number can't be empty!!");
            return false;
        } else if (mobile.length() < 10) {
            authSignInMobile.setError("Mobile Number too short!!");
            return false;
        } else if (mobile.length() > 10) {
            authSignInMobile.setError("Mobile Number too long!!");
            return false;
        }
        authSignInMobile.setError(null);
        return true;
    }
}
