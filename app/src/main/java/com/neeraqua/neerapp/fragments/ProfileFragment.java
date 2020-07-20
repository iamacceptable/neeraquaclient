package com.neeraqua.neerapp.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputLayout;
import com.neeraqua.neerapp.MainActivity;
import com.neeraqua.neerapp.R;
import com.neeraqua.neerapp.TerminalConstant;
import com.neeraqua.neerapp.response.CityResponse;
import com.neeraqua.neerapp.response.CommonResponse;
import com.neeraqua.neerapp.response.LocalityResponse;
import com.neeraqua.neerapp.response.User;
import com.neeraqua.neerapp.response.UserProfileResponse;
import com.neeraqua.neerapp.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.neeraqua.neerapp.TerminalConstant.MY_PREFS;
import static com.neeraqua.neerapp.TerminalConstant.USER_ID_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment {

    private TextInputLayout signUpMobile;
    private TextInputLayout signUpName;
    private TextInputLayout signUpPassword;
    private TextInputLayout signUpHouseNo;
    private Spinner city;
    private Spinner state;
    private Spinner localitySpinner;
    private Button editProfileBtn;
    private String mStateSelected = "";
    private String mCitySelected = "";
    private String mLocalitySelected = "";

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        signUpMobile = v.findViewById(R.id.profileMobile);
        signUpName = v.findViewById(R.id.profileName);
        signUpPassword = v.findViewById(R.id.profilePassword);
        signUpHouseNo = v.findViewById(R.id.profileHouseNO);
        city = v.findViewById(R.id.city);
        state = v.findViewById(R.id.state);
        localitySpinner = v.findViewById(R.id.localitySpinner);
        editProfileBtn = v.findViewById(R.id.editProfileBtn);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfileServerCall();
            }
        });
        getUserProfileFromServer();
        return v;
    }

    private void changeProfileServerCall() {
        showProgress();
        String newUserName = signUpName.getEditText().getText().toString().trim();
        String newMobileNumber = signUpMobile.getEditText().getText().toString().trim();
        String newHouseNumber = signUpHouseNo.getEditText().getText().toString().trim();
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        String loginId = prefs.getString(USER_ID_KEY, "");
        RetrofitApi.getPaniServicesObject().getUpdateProfileResponse(loginId, newUserName,
                                                                     newMobileNumber,
                                                                     newHouseNumber, mStateSelected,
                                                                     mCitySelected, mLocalitySelected).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = response.body();
                    if (commonResponse != null && commonResponse.getErrorCode().equals(TerminalConstant.SUCCESS)) {
                        showToast(commonResponse.getErrorMessage());
                        FragmentLauncher.launchFragment(getActivity(), R.id.mainFrameLayout, new HomeFragment(), false, true);
                    } else if (commonResponse != null) {
                        showToast(commonResponse.getErrorMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                stopProgress();
                showToast(t.toString());
            }
        });
    }

    @Override
    public void backPressed() {
        MainActivity.toolbar.setTitle(getResources().getString(R.string.app_name));
        FragmentLauncher.launchFragment(getActivity(), R.id.mainFrameLayout, new HomeFragment(), false, true);
    }

    private void getUserProfileFromServer() {
        showProgress();
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        String loginId = prefs.getString(USER_ID_KEY, "");
        RetrofitApi.getPaniServicesObject().getUserProfileResponse(loginId).enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserProfileResponse> call, @NonNull Response<UserProfileResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    UserProfileResponse userProfileResponse = response.body();
                    if (userProfileResponse != null && userProfileResponse.getErrorCode() != null) {
                        if (userProfileResponse.getErrorCode().equals(TerminalConstant.SUCCESS)) {
                            List<User> userList = userProfileResponse.getUser();
                            User user = userList.get(0);
                            signUpMobile.getEditText().setText(user.getUMobile());
                            signUpName.getEditText().setText(user.getUName());
                            signUpHouseNo.getEditText().setText(user.getUHno());
                            ArrayAdapter<String> dataAdapterState = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, sStateList);
                            dataAdapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            state.setAdapter(dataAdapterState);
                            state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    mStateSelected = sStateList.get(i);
                                    getCitySpinner();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    mStateSelected = "";
                                }
                            });
                        } else {
                            showToast("No Profile available yet for the current user");
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserProfileResponse> call, @NonNull Throwable t) {
                stopProgress();
                showToast(t.toString());
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
                stopProgress();
                if (response.isSuccessful()) {
                    LocalityResponse locationResponse = response.body();
                    if (locationResponse != null && locationResponse.getLocalities() != null && !locationResponse.getLocalities().isEmpty()) {
                        final List<String> locationList = locationResponse.getLocalities();
                        ArrayAdapter<String> dataAdapterCity = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, locationList);
                        dataAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        localitySpinner.setAdapter(dataAdapterCity);
                        localitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

}
