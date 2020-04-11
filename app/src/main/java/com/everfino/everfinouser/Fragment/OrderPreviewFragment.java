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
import com.everfino.everfinouser.Adapter.OrderPreviewAdapter;
import com.everfino.everfinouser.ApiConnection.Api;
import com.everfino.everfinouser.ApiConnection.ApiClient;
import com.everfino.everfinouser.AppSharedPreferences;
import com.everfino.everfinouser.Models.Liveorder;
import com.everfino.everfinouser.Models.Order;
import com.everfino.everfinouser.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderPreviewFragment extends Fragment {


    RecyclerView rcv_menu_order;
    OrderPreviewAdapter adapter;
    EditText searchorder;
    List<HashMap<String,String>> order=new ArrayList<>();
    int tableid,restid;
    Button placebtn;
    private static Api apiService;
    public OrderPreviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("###","xyz");

        View view= inflater.inflate(R.layout.fragment_order_preview, container, false);
        restid=getArguments().getInt("restid");
        tableid=getArguments().getInt("tableid");

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
        placebtn=view.findViewById(R.id.placebtn);
        placebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addbill();
            }
        });

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


    public void  addbill()
    {
        AppSharedPreferences appSharedPreferences=new AppSharedPreferences(getContext());
        final HashMap<String,String> map=appSharedPreferences.getPref();
        JsonObject object=new JsonObject();
        object.addProperty("userid",map.get("userid"));
        int amount=0;
        for(HashMap<String,String> i : order)
        {
            amount=amount+(Integer.parseInt(i.get("itemprice"))*Integer.parseInt(i.get("orderquntity")));
        }

        object.addProperty("amount",amount);
        object.addProperty("paymentstatus","Pendding");
        Call<Order> call=apiService.place_Order(restid,object);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if(response.body().getOrderid()!=0)
                {
                    for(HashMap<String,String> i :order)
                    {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("orderid", response.body().getOrderid());
                    jsonObject.addProperty("tableid", tableid);
                    jsonObject.addProperty("itemid",i.get("itemid"));
                    jsonObject.addProperty("userid",map.get("userid"));
                    jsonObject.addProperty("quntity",i.get("orderquntity"));
                    jsonObject.addProperty("status", "pendding");

                    Call<Liveorder> call1=apiService.add_Live_Order(restid,jsonObject);
                    call1.enqueue(new Callback<Liveorder>() {
                        @Override
                        public void onResponse(Call<Liveorder> call, Response<Liveorder> response) {
                            Log.e("###",response.body().toString());
                            Toast.makeText(getContext(),response.body().toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Liveorder> call, Throwable t) {

                        }
                    });


                    }
                    Fragment fragment=new HomeFragment();
                    loadFragment(fragment);

                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {

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
