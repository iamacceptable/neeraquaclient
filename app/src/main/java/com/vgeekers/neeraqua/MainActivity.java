package com.vgeekers.neeraqua;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.navigation.NavigationView;
import com.vgeekers.neeraqua.fragments.BaseFragment;
import com.vgeekers.neeraqua.fragments.ContactUsFragment;
import com.vgeekers.neeraqua.fragments.HomeFragment;
import com.vgeekers.neeraqua.fragments.Orders;
import com.vgeekers.neeraqua.fragments.PrivacyPolicyFragment;
import com.vgeekers.neeraqua.fragments.ProfileFragment;
import com.vgeekers.neeraqua.fragments.TermsConditionsFragment;

import java.util.List;

import static com.vgeekers.neeraqua.TerminalConstant.MY_PREFS;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    public static Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, new HomeFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getCurrentFragment() != null) {
                getCurrentFragment().backPressed();
            } else {
                super.onBackPressed();
            }
        }
    }

    public BaseFragment getCurrentFragment() {
        FragmentManager mgr = getSupportFragmentManager();
        List<Fragment> list = mgr.getFragments();
        int count = mgr.getBackStackEntryCount();
        if (0 == count) {
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) != null && list.get(i) instanceof BaseFragment) {
                        return (BaseFragment) list.get(i);
                    }
                }
            }
            return null;
        }
        FragmentManager.BackStackEntry entry = mgr.getBackStackEntryAt(count - 1);
        return (BaseFragment) mgr.findFragmentByTag(entry.getName());
    }

    private void appShare() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            Log.e(MainActivity.class.getSimpleName(), e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_notification) {
            Toast.makeText(this, "Notification Recieved", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, new HomeFragment()).commit();
            }
        } else if (id == R.id.nav_profile) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Profile");
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, new ProfileFragment()).commit();
            }
        } else if (id == R.id.nav_privacy_policy) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Privacy Policy");
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, new PrivacyPolicyFragment()).commit();
            }
        } else if (id == R.id.my_orders) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("My Orders");
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, new Orders()).commit();
            }
        }else if (id == R.id.nav_terms_conditions) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Terms & Conditons");
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, new TermsConditionsFragment()).commit();
            }
        }else if (id == R.id.nav_contact_us) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Contact Us");
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, new ContactUsFragment()).commit();
            }
        } else if (id == R.id.nav_share) {
            appShare();
        } else if (id == R.id.nav_logout) {
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
            editor.putBoolean(TerminalConstant.USER_LOGIN_DONE_KEY, false);
            editor.apply();
            startActivity(new Intent(getApplicationContext(), Authentication.class));
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
