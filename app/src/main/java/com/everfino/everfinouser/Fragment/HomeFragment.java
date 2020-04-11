package com.everfino.everfinouser.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.everfino.everfinouser.Adapter.RestAdapter;
import com.everfino.everfinouser.ApiConnection.Api;
import com.everfino.everfinouser.ApiConnection.ApiClient;
import com.everfino.everfinouser.Models.RestList;
import com.everfino.everfinouser.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    RestAdapter adapter;
    RecyclerView rcv_rest;
    FloatingActionButton rest_add_btn;
    EditText serarchrest;
    List<HashMap<String,String>> ls=new ArrayList<>();
    private static Api apiService;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_home, container, false);
        rcv_rest=view.findViewById(R.id.rcv_rest);

        apiService= ApiClient.getClient().create(Api.class);
        serarchrest=view.findViewById(R.id.searchrest);
        serarchrest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fetch_rest();
        return view;
    }
    private void filter(String text) {

        List<HashMap<String,String>> l=new ArrayList<>();

        for (HashMap<String,String> s : ls) {
            Log.e("abcccccc",s.toString());
            if (s.toString().toLowerCase().contains(text.toLowerCase())) {
                Log.e("true", String.valueOf(s));
                l.add(s);
            }
        }

        adapter.filterList(l);
        adapter.notifyDataSetChanged();

    }

    private void fetch_rest(){

        ls.clear();
        rcv_rest.setLayoutManager(new GridLayoutManager(getContext(),1));

        Call<List<RestList>> call=apiService.get_Rest();
        call.enqueue(new Callback<List<RestList>>() {
            @Override
            public void onResponse(Call<List<RestList>> call, Response<List<RestList>> response) {
                for(RestList item: response.body()) {

                    HashMap<String,String> map=new HashMap<>();
                    map.put("restid",item.getRestid()+"");
                    map.put("mobileno",item.getMobileno());
                    map.put("email",item.getEmail());
                    map.put("restname",item.getRestname());
                    map.put("restdesc",item.getRestdesc());
                    map.put("address",item.getAddress());
                    map.put("city",item.getCity());
                    map.put("status",item.getStatus());
                    ls.add(map);
                }

                adapter=new RestAdapter(getContext(),ls);
                rcv_rest.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<RestList>> call, Throwable t) {
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
