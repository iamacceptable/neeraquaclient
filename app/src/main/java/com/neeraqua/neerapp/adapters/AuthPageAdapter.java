package com.neeraqua.neerapp.adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.neeraqua.neerapp.fragments.SignIn;
import com.neeraqua.neerapp.fragments.SignUp;

public class AuthPageAdapter extends FragmentStatePagerAdapter {
    String[] authArray = new String[]{"Sign In", "Sign Up"};
    Integer count = 2;

    public AuthPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return authArray[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: SignIn signIn = new SignIn();
                return signIn;
            case 1: SignUp signUp = new SignUp();
                return signUp;
        }
        return null;
    }

    @Override
    public int getCount() {
        return count;
    }
}
