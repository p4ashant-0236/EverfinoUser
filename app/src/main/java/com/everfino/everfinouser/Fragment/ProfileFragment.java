package com.everfino.everfinouser.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.everfino.everfinouser.AppSharedPreferences;
import com.everfino.everfinouser.LoginActivity;
import com.everfino.everfinouser.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    Button logout;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        logout=view.findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppSharedPreferences appSharedPreferences=new AppSharedPreferences(getContext());
                appSharedPreferences.clearPref();
                Intent i=new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
            }
        });
        return view;
    }

}
