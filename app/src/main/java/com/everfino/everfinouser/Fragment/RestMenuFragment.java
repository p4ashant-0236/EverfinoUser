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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.everfino.everfinouser.Adapter.MenuAdapter;
import com.everfino.everfinouser.ApiConnection.Api;
import com.everfino.everfinouser.ApiConnection.ApiClient;
import com.everfino.everfinouser.AppSharedPreferences;
import com.everfino.everfinouser.Models.MenuList;
import com.everfino.everfinouser.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestMenuFragment extends Fragment {

    AppSharedPreferences appSharedPreferences;
    HashMap<String,String> map;
    RecyclerView rcv_menu;
    MenuAdapter adapter;
    EditText searchmenu;
    Button placeorder;
    List<HashMap<String,String>> ordereditem=new ArrayList<>();

    List<HashMap<String, String>> ls_menu = new ArrayList<>();
    private static Api apiService;
    public RestMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_rest_menu, container, false);
        rcv_menu = view.findViewById(R.id.rcv_menu);
        apiService = ApiClient.getClient().create(Api.class);
        appSharedPreferences=new AppSharedPreferences(getContext());
        placeorder=view.findViewById(R.id.placeorder);
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ordereditem=adapter.getSelected();


                ArrayList<String> itemname=new ArrayList<>();
                ArrayList<String> itemprice=new ArrayList<>();
                ArrayList<String> orderquntity=new ArrayList<>();
                ArrayList<String> itemid=new ArrayList<>();
                for (HashMap<String,String> item : ordereditem)
                {
                        itemid.add(item.get("itemid"));
                        itemname.add(item.get("itemname"));
                        itemprice.add(item.get("itemprice"));
                        orderquntity.add(item.get("orderquntity"));
                }
                Bundle b=new Bundle();
                b.putInt("tableid",getArguments().getInt("tableid"));
                b.putStringArrayList("itemid",itemid);
                b.putStringArrayList("itemname",itemname);
                b.putStringArrayList("itemprice",itemprice);
                b.putStringArrayList("orderquntity",orderquntity);

                Fragment fragment=new OrderPreviewFragment();
                fragment.setArguments(b);
                Log.e("###","abc");
                loadFragment(fragment);
            }
        });
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
        Call<List<MenuList>> call = apiService.get_Rest_Menu(getArguments().getInt("restid"));
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
                    map.put("orderquntity","0");
                    Log.e("####", item.getItemname());
                    ls_menu.add(map);
                }

                adapter = new MenuAdapter(getContext(), ls_menu);
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
