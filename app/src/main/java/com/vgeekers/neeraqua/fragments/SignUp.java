package com.vgeekers.neeraqua.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.vgeekers.neeraqua.Authentication;
import com.vgeekers.neeraqua.R;
import com.vgeekers.neeraqua.TerminalConstant;
import com.vgeekers.neeraqua.response.CityResponse;
import com.vgeekers.neeraqua.response.CommonResponse;
import com.vgeekers.neeraqua.response.FetchAllStatesResponse;
import com.vgeekers.neeraqua.response.LocalityResponse;
import com.vgeekers.neeraqua.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUp extends BaseFragment {

    private TextInputLayout signUpMobile;
    private TextInputLayout signUpName;
    private TextInputLayout signUpPassword;
    private TextInputLayout signUphouseNo;
    private Spinner signUpLocality;
    private Spinner city;
    private Spinner state;
    private String mStateSelected = "";
    private String mCitySelected = "";
    private String mLocalitySelected = "";

    public SignUp() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);
        signUpMobile = v.findViewById(R.id.signUpMobile);
        signUpName = v.findViewById(R.id.signUpName);
        signUpPassword = v.findViewById(R.id.signUpPassword);
        signUphouseNo = v.findViewById(R.id.signUpHouseNO);
        signUpLocality = v.findViewById(R.id.signUpLocality);
        city = v.findViewById(R.id.city);
        state = v.findViewById(R.id.state);
        Button signUpBtn = v.findViewById(R.id.signUpBtn);
        setupStateSpinner1();
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFacebookOtpForVerification();
            }
        });
        return v;
    }

    private void setupStateSpinner1() {
        RetrofitApi.getPaniServicesObject().getAllStates().enqueue(new Callback<FetchAllStatesResponse>() {
            @Override
            public void onResponse(@NonNull Call<FetchAllStatesResponse> call, @NonNull Response<FetchAllStatesResponse> response) {
                if (response.isSuccessful()) {
                    FetchAllStatesResponse statesResponse = response.body();
                    if (statesResponse != null && statesResponse.getStates() != null && !statesResponse.getStates().isEmpty() && getContext() != null) {
                        final List<String> stateList = statesResponse.getStates();
                        sStateList = stateList;
                        ArrayAdapter<String> dataAdapterState = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, stateList);
                        dataAdapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        state.setAdapter(dataAdapterState);
                        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                mStateSelected = stateList.get(i);
                                getCitySpinner();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                mStateSelected = "";
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FetchAllStatesResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCitySpinner() {
        RetrofitApi.getPaniServicesObject().getCitiesResponse(mStateSelected).enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(@NonNull Call<CityResponse> call, @NonNull Response<CityResponse> response) {
                if (response.isSuccessful()) {
                    CityResponse cityResponse = response.body();
                    if (cityResponse != null && cityResponse.getCities() != null && !cityResponse.getCities().isEmpty()) {
                        final List<String> listCity = cityResponse.getCities();
                        ArrayAdapter<String> dataAdapterCity = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listCity);
                        dataAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        city.setAdapter(dataAdapterCity);
                        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                mCitySelected = listCity.get(i);
                                getLocalitiesSpinner();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                mCitySelected = "";
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CityResponse> call, @NonNull Throwable t) {
                showToast(t.toString());
            }
        });
    }

    private void getLocalitiesSpinner() {
        RetrofitApi.getPaniServicesObject().getLocalitiesResponse(mCitySelected).enqueue(new Callback<LocalityResponse>() {
            @Override
            public void onResponse(@NonNull Call<LocalityResponse> call, @NonNull Response<LocalityResponse> response) {
                if (response.isSuccessful()) {
                    LocalityResponse locationResponse = response.body();
                    if (locationResponse != null && locationResponse.getLocalities() != null && !locationResponse.getLocalities().isEmpty()) {
                        final List<String> locationList = locationResponse.getLocalities();
                        ArrayAdapter<String> dataAdapterCity = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, locationList);
                        dataAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        signUpLocality.setAdapter(dataAdapterCity);
                        signUpLocality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                mLocalitySelected = locationList.get(i);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                mLocalitySelected = "";
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LocalityResponse> call, @NonNull Throwable t) {
                showToast(t.toString());
            }
        });
    }

    private void getSignUpApiCall() {
        String userMobile = signUpMobile.getEditText().getText().toString().trim();
        String userName = signUpName.getEditText().getText().toString().trim();
        String userPassword = signUpPassword.getEditText().getText().toString().trim();
        String userHouseNumber = signUphouseNo.getEditText().getText().toString().trim();
        String token = FirebaseInstanceId.getInstance().getToken();
        showProgress();
        RetrofitApi.getPaniServicesObject().getSignUpResponse(userName, userMobile, userPassword, userHouseNumber, mStateSelected, mCitySelected, mLocalitySelected, token).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = response.body();
                    if (commonResponse != null && commonResponse.getErrorCode().equals(TerminalConstant.SUCCESS)) {
                        Toast.makeText(getContext(), "SignUp Successful, please login again to confirm your credentials", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), Authentication.class));
                    } else if (commonResponse != null && commonResponse.getErrorCode() != null) {
                        showToast(commonResponse.getErrorMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                stopProgress();
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
     * Facebook Otp verification
     * */

    private static final int APP_REQUEST_CODE = 99;

    private void sendFacebookOtpForVerification() {
        final Intent intent = new Intent(getActivity(), AccountKitActivity.class);
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
            }
            // Surface the result to your user in an appropriate way.
            showToast(toastMessage);
            getSignUpApiCall();
        }
    }

    private void facebookLogout() {
        AccountKit.logOut();
    }
}
