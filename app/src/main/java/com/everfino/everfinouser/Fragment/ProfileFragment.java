package com.everfino.everfinouser.Fragment;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.everfino.everfinouser.ApiConnection.Api;
import com.everfino.everfinouser.ApiConnection.ApiClient;
import com.everfino.everfinouser.AppSharedPreferences;
import com.everfino.everfinouser.LoginActivity;
import com.everfino.everfinouser.Models.User;
import com.everfino.everfinouser.R;

import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    Button logout;
    Button Edituserbtn, editinfobtn;
    TextView email, mobile, name, gender, dob;
    EditText eemail, epassword, cepassword, emobileno, ename, edob;
    Spinner egender;
    LinearLayout details, edit;
    int mYear, mMonth, mDay;

    private static Api apiService;
    AppSharedPreferences appSharedPreferences;
    HashMap<String, String> map;
    String g[] = {"Male", "Female"};
    User u;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        logout = view.findViewById(R.id.logout_btn);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppSharedPreferences appSharedPreferences = new AppSharedPreferences(getContext());
                appSharedPreferences.clearPref();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
            }
        });
        name = view.findViewById(R.id.name);
        gender = view.findViewById(R.id.gender);
        dob = view.findViewById(R.id.dob);
        email = view.findViewById(R.id.email);
        mobile = view.findViewById(R.id.mobileno);
        ename = view.findViewById(R.id.ename);
        edob = view.findViewById(R.id.edob);
        egender = view.findViewById(R.id.egender);
        editinfobtn = view.findViewById(R.id.editinfo);
        cepassword = view.findViewById(R.id.cepassword);
        eemail = view.findViewById(R.id.eemail);
        epassword = view.findViewById(R.id.epassword);
        emobileno = view.findViewById(R.id.emobileno);
        Edituserbtn = view.findViewById(R.id.editbtn);
        details = view.findViewById(R.id.userdatails);
        edit = view.findViewById(R.id.edituserdetails);

        mDay= Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        mMonth=Calendar.getInstance().get(Calendar.MONTH);
        mYear=Calendar.getInstance().get(Calendar.YEAR);
        edob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dataDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        dob.setText(date+"-"+month+"-"+year);
                    }
                },mYear,mMonth,mDay);
                dataDialog.show();
            }
        });
        ArrayAdapter<String> data = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, g);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        egender.setAdapter(data);

        apiService = ApiClient.getClient().create(Api.class);
        appSharedPreferences = new AppSharedPreferences(getContext());

        load_data();
        edit.setVisibility(View.GONE);

        editinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);

            }
        });


        Edituserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_data();
            }
        });

        return view;
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void load_data() {
        Log.e("####", "sfs");
        map = appSharedPreferences.getPref();
        Call<User> call = apiService.get_User(Integer.parseInt(map.get("userid")));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                email.setText(response.body().getEmail());
                mobile.setText(response.body().getMobileno());
                gender.setText(response.body().getGender());
                name.setText(response.body().getName());
                dob.setText(response.body().getDob());


                eemail.setText(response.body().getEmail());
                emobileno.setText(response.body().getMobileno());
                epassword.setText(response.body().getPassword());
                edob.setText(response.body().getDob());
                ename.setText(response.body().getName());
                u = response.body();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("####", t.getMessage());
            }
        });
    }


    public void update_data() {
        map = appSharedPreferences.getPref();
        if (ename.getText().length() == 0) {
            ename.setError("Name is Required!");
        } else if (emobileno.getText().length() == 0) {
            emobileno.setError("Mobile No is Required!");
        } else if (epassword.getText().length() == 0) {
            epassword.setError("Password is Required!");
        } else if (epassword.getText().length() != cepassword.getText().length()) {
            cepassword.setError("Password and Confirm password  is not same");
        } else if (eemail.getText().length() == 0) {
            eemail.setError("Email is Required!");
        } else {
            u.setName(ename.getText().toString());
            u.setMobileno(emobileno.getText().toString());
            u.setGender(egender.getSelectedItem().toString());
            u.setPassword(epassword.getText().toString());
            u.setEmail(eemail.getText().toString());

            Call<User> call = apiService.update_User(u.userid, u);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    ProfileFragment profileFragment = new ProfileFragment();
                    loadFragment(profileFragment);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }

}
