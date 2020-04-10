package com.everfino.everfinouser.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

import com.everfino.everfinouser.Adapter.MenuAdapter;
import com.everfino.everfinouser.Adapter.OrderPreviewAdapter;
import com.everfino.everfinouser.ApiConnection.Api;
import com.everfino.everfinouser.ApiConnection.ApiClient;
import com.everfino.everfinouser.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderPreviewFragment extends Fragment {


    RecyclerView rcv_menu_order;
    OrderPreviewAdapter adapter;
    EditText searchorder;
    List<HashMap<String,String>> order=new ArrayList<>();
    private static Api apiService;
    public OrderPreviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("###","xyz");

        View view= inflater.inflate(R.layout.fragment_order_preview, container, false);

        apiService= ApiClient.getClient().create(Api.class);
        for(int i=0;i<getArguments().getStringArrayList("itemid").size();i++)
        {
            HashMap<String,String> map=new HashMap<>();
            map.put("itemid",getArguments().getStringArrayList("itemid").get(i));
            map.put("itemname",getArguments().getStringArrayList("itemname").get(i));
            map.put("itemprice",getArguments().getStringArrayList("itemprice").get(i));
            map.put("orderquntity",getArguments().getStringArrayList("orderquntity").get(i));
            order.add(map);
        }

        rcv_menu_order=view.findViewById(R.id.rcv_menu_order);
        searchorder=view.findViewById(R.id.searchorder);
        searchorder.addTextChangedListener(new TextWatcher() {
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

        for (HashMap<String,String> s : order) {
            Log.e("abcccccc",s.toString());
            if (s.toString().toLowerCase().contains(text.toLowerCase())) {
                Log.e("true", String.valueOf(s));
                ls.add(s);
            }
        }

        adapter.filterList(ls);

    }
    private void fetch_menu() {

        rcv_menu_order.setLayoutManager(new GridLayoutManager(getContext(), 1));

                adapter = new OrderPreviewAdapter(getContext(), order);
                rcv_menu_order.setAdapter(adapter);
            }






}
