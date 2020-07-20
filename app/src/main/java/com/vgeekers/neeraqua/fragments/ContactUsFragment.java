package com.vgeekers.neeraqua.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vgeekers.neeraqua.MainActivity;
import com.vgeekers.neeraqua.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends BaseFragment {


    TextView call, email;

    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        call = view.findViewById(R.id.phon);
        email = (TextView) view.findViewById(R.id.email);


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  Intent callIntent = new Intent(Intent.ACTION_CALL);
                Intent callintent = new Intent(Intent.ACTION_DIAL);
                callintent.setData(Uri.parse("tel:+91 99719 72314"));
                startActivity(callintent);
                /*callIntent.setFlags(Intent.FLAG_ACTIVITY_NEcaW_TASK);
                callIntent.setData(Uri.parse("tel:" + "+91 99719 72314"));
                if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                getActivity().startActivity(callIntent);*/
            }
        });


        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Send email", "");
                String[] TO = {""};
                String[] CC = {""};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    getActivity().finish();
                    Log.i("Finished sending email", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }



        });

        return view;
    }




    @Override
    public void backPressed() {
        MainActivity.toolbar.setTitle(getResources().getString(R.string.app_name));
        FragmentLauncher.launchFragment(getActivity(), R.id.mainFrameLayout, new HomeFragment(), false, true);
    }
}
