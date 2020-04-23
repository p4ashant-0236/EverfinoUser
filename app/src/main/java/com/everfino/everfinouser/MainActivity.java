package com.everfino.everfinouser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.everfino.everfinouser.Fragment.HistoryFragment;
import com.everfino.everfinouser.Fragment.HomeFragment;
import com.everfino.everfinouser.Fragment.LiveOrderFragment;
import com.everfino.everfinouser.Fragment.ProfileFragment;
import com.everfino.everfinouser.Fragment.ReservationFragment;
import com.everfino.everfinouser.Fragment.ScanFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView tab_menu;
    AppSharedPreferences appSharedPreferences;
    HashMap<String,String> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appSharedPreferences=new AppSharedPreferences(this);

        map=appSharedPreferences.getPref();

        if(Integer.parseInt(map.get("userid"))==0 && map.get("email")=="")
        {
            Intent i=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
        }
        else
        {
            Log.e("###############",map.get("adminid")+map.get("username"));
        }

        tab_menu = findViewById(R.id.tab_menu);

        tab_menu.setSelectedItemId(R.id.nav_home);
        Fragment fragment=new HomeFragment();
        loadFragment(fragment);

        tab_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.nav_profile:

                        //LiveOrder

                        fragment = new ProfileFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.nav_history:
                        //Table

                        fragment = new HistoryFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.nav_liveorder:
                        //Menu
                        fragment = new LiveOrderFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.nav_scan:
                        //Statistic

                        fragment = new ScanFragment();
                        loadFragment(fragment);
                        break;

                    case R.id.nav_home:
                        //Statistic

                        fragment = new HomeFragment();
                        loadFragment(fragment);
                        break;
                }
                return true;
            }
        });
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
