package com.vgeekers.neeraqua.fragments;

import android.app.ProgressDialog;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.vgeekers.neeraqua.response.FetchAllStatesResponse;
import com.vgeekers.neeraqua.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class BaseFragment extends Fragment {

    protected static List<String> sStateList = new ArrayList<>();
    protected ProgressDialog mProgressDialog;

    protected void showProgress() {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setTitle("Processing");
        mProgressDialog.setMessage("Please Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
    }

    protected void stopProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void showToast(String msg) {
        try {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            /*
             * Just to holding the crashes
             * */
        }
    }

    public void backPressed() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    void setupStateSpinner() {
        RetrofitApi.getPaniServicesObject().getAllStates().enqueue(new Callback<FetchAllStatesResponse>() {
            @Override
            public void onResponse(@NonNull Call<FetchAllStatesResponse> call, @NonNull Response<FetchAllStatesResponse> response) {
                if (response.isSuccessful()) {
                    FetchAllStatesResponse statesResponse = response.body();
                    if (statesResponse != null && statesResponse.getStates() != null && !statesResponse.getStates().isEmpty() && getContext() != null) {
                        sStateList = statesResponse.getStates();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FetchAllStatesResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
