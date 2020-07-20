package com.neeraqua.neerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.neeraqua.neerapp.fragments.BaseFragment;
import com.neeraqua.neerapp.fragments.FragmentLauncher;
import com.neeraqua.neerapp.fragments.HomeFragment;

public class ThankingPage extends BaseFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_thanking_page, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void backPressed() {
        MainActivity.toolbar.setTitle(getResources().getString(R.string.app_name));
        FragmentLauncher.launchFragment(getActivity(), R.id.mainFrameLayout, new HomeFragment(), false, true);
    }
}
