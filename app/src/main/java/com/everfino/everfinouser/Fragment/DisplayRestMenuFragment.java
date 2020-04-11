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

import com.everfino.everfinouser.Adapter.RestMenuAdapter;
import com.everfino.everfinouser.ApiConnection.Api;
import com.everfino.everfinouser.ApiConnection.ApiClient;
import com.everfino.everfinouser.AppSharedPreferences;
import com.everfino.everfinouser.Models.MenuList;
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
public class DisplayRestMenuFragment extends Fragment {

    AppSharedPreferences appSharedPreferences;
    HashMap<String,String> map;
    RecyclerView rcv_menu;


    RestMenuAdapter adapter;
    EditText searchmenu;
    List<HashMap<String, String>> ls_menu = new ArrayList<>();
    private static Api apiService;

    public DisplayRestMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_display_rest_menu, container, false);
        rcv_menu = view.findViewById(R.id.rcv_menu);

        apiService = ApiClient.getClient().create(Api.class);
        appSharedPreferences=new AppSharedPreferences(getContext());

        searchmenu=view.findViewById(R.id.searchmenu);
        searchmenu.addTextChangedListener(new TextWatcher() {
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
        fetch_menu();
        return view;
    }
    private void filter(String text) {

        List<HashMap<String,String>> ls=new ArrayList<>();

        for (HashMap<String,String> s : ls_menu) {
            Log.e("abcccccc",s.toString());
            if (s.toString().toLowerCase().contains(text.toLowerCase())) {
                Log.e("true", String.valueOf(s));
                ls.add(s);
            }
        }

        adapter.filterList(ls);

    }
    private void fetch_menu() {

        ls_menu.clear();
        rcv_menu.setLayoutManager(new GridLayoutManager(getContext(), 1));
        map=appSharedPreferences.getPref();
        Call<List<MenuList>> call = apiService.get_Rest_Menu(Integer.parseInt(getArguments().getString("restid")));
        call.enqueue(new Callback<List<MenuList>>() {
            @Override
            public void onResponse(Call<List<MenuList>> call, Response<List<MenuList>> response) {
                for (MenuList item : response.body()) {

                    HashMap<String, String> map = new HashMap<>();
                    map.put("itemid", item.getItemid() + "");
                    map.put("itemname", item.getItemname());
                    map.put("itemprice", item.getItemprice() + "");
                    map.put("itemtype", item.getItemtype());
                    map.put("itemdesc", item.getItemdesc());
                    Log.e("####", item.getItemname());
                    ls_menu.add(map);
                }

                adapter = new RestMenuAdapter(getContext(), ls_menu);
                rcv_menu.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<MenuList>> call, Throwable t) {
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
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
