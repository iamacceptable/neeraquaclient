package com.neeraqua.neerapp.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentLauncher {

    private FragmentLauncher() {
    }

    public static void launchFragment(Activity activity, @IdRes int activityId, Fragment fragment, boolean addBackStack, boolean clearBackStack) {
        FragmentManager manager = ((AppCompatActivity) activity).getSupportFragmentManager();
        if (clearBackStack) {
            manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (addBackStack) {
            manager.beginTransaction()
                   .addToBackStack(null)
                   .replace(activityId, fragment)
                   .commit();
        } else {
            manager.beginTransaction()
                   .replace(activityId, fragment)
                   .commit();
        }
    }

    @SuppressLint("NewApi")
    public static void launchFragmentV2(Activity activity, @IdRes int activityId, Fragment fragment, boolean addBackStack, boolean clearBackStack, View view) {
        FragmentManager manager = ((AppCompatActivity) activity).getSupportFragmentManager();
        if (clearBackStack) {
            manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (addBackStack) {
            manager.beginTransaction()
                   .addToBackStack(null)
                   .replace(activityId, fragment)
                   .addSharedElement(view, ViewCompat.getTransitionName(view))
                   .commit();
        } else {
            manager.beginTransaction()
                   .addSharedElement(view, ViewCompat.getTransitionName(view))
                   .replace(activityId, fragment)
                   .commit();
        }
    }
}
