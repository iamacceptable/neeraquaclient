package com.neeraqua.neerapp.fragments;

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
import com.google.android.material.textfield.TextInputLayout;
import com.neeraqua.neerapp.R;
import com.neeraqua.neerapp.SignUpOtpActivity;
import com.neeraqua.neerapp.response.CityResponse;
import com.neeraqua.neerapp.response.FetchAllStatesResponse;
import com.neeraqua.neerapp.response.LocalityResponse;
import com.neeraqua.neerapp.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                Intent intent = new Intent(getActivity(), SignUpOtpActivity.class);
                String userMobile = signUpMobile.getEditText().getText().toString().trim();
                String userName = signUpName.getEditText().getText().toString().trim();
                String userPassword = signUpPassword.getEditText().getText().toString().trim();
                String userHouseNumber = signUphouseNo.getEditText().getText().toString().trim();
                intent.putExtra("mobile", userMobile);
                intent.putExtra("name", userName);
                intent.putExtra("pass", userPassword);
                intent.putExtra("hno", userHouseNumber);
                intent.putExtra("state", mStateSelected);
                intent.putExtra("city", mCitySelected);
                intent.putExtra("locality", mLocalitySelected);
                startActivity(intent);
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
}
