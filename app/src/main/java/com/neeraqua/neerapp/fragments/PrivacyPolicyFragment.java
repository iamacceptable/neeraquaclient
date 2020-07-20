package com.neeraqua.neerapp.fragments;

import android.os.Bundle;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neeraqua.neerapp.MainActivity;
import com.neeraqua.neerapp.R;
import com.neeraqua.neerapp.response.DetailResponse;
import com.neeraqua.neerapp.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrivacyPolicyFragment extends BaseFragment {

    private TextView mPrivacyDataTextView;

    public PrivacyPolicyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        mPrivacyDataTextView = view.findViewById(R.id.privacyPolicyDataTextView);
        getPrivacyPolicy();
        return view;
    }

    private void getPrivacyPolicy() {
        showProgress();
        RetrofitApi.getPaniServicesObject().fetchPrivacyPolicy().enqueue(new Callback<DetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<DetailResponse> call, @NonNull Response<DetailResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    DetailResponse detail = response.body();
                    if (detail != null && detail.getDetail() != null && !detail.getDetail().isEmpty()) {
                        mPrivacyDataTextView.setText(detail.getDetail());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DetailResponse> call, @NonNull Throwable t) {
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
}
