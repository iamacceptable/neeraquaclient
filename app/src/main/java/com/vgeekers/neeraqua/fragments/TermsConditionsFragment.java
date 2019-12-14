package com.vgeekers.neeraqua.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.vgeekers.neeraqua.MainActivity;
import com.vgeekers.neeraqua.R;
import com.vgeekers.neeraqua.response.DetailResponse;
import com.vgeekers.neeraqua.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TermsConditionsFragment extends BaseFragment {

    private TextView mTncDataTextView;

    public TermsConditionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_terms_conditions, container, false);
        mTncDataTextView = view.findViewById(R.id.tncDataTextView);
        getTncResponse();
        return view;
    }

    @Override
    public void backPressed() {
        MainActivity.toolbar.setTitle(getResources().getString(R.string.app_name));
        FragmentLauncher.launchFragment(getActivity(), R.id.mainFrameLayout, new HomeFragment(), false, true);
    }

    private void getTncResponse() {
        showProgress();
        RetrofitApi.getPaniServicesObject().fetchTncResponse().enqueue(new Callback<DetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<DetailResponse> call, @NonNull Response<DetailResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    DetailResponse detail = response.body();
                    if (detail != null && detail.getDetail() != null && !detail.getDetail().isEmpty()) {
                        mTncDataTextView.setText(detail.getDetail());
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
}
