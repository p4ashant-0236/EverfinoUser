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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.everfino.everfinouser.Adapter.LiveOrderAdapter;
import com.everfino.everfinouser.ApiConnection.Api;
import com.everfino.everfinouser.ApiConnection.ApiClient;
import com.everfino.everfinouser.AppSharedPreferences;
import com.everfino.everfinouser.Models.Liveorder;
import com.everfino.everfinouser.Models.RestList;
import com.everfino.everfinouser.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveOrderFragment extends Fragment {
    private static Api apiService;
    Spinner restlist;
    RecyclerView rcv_liveorder;
    EditText search;

    public LiveOrderFragment() {
        // Required empty public constructor
    }
    String[] restname;
    int[] restid;

    LiveOrderAdapter adapter;
    List<HashMap<String,String>> ls_order = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        apiService = ApiClient.getClient().create(Api.class);

        View view= inflater.inflate(R.layout.fragment_live_order, container, false);
        Call<List<RestList>> call=apiService.get_Rest();
        restlist=view.findViewById(R.id.restlist);
        search=view.findViewById(R.id.searchliveorder);
        search.addTextChangedListener(new TextWatcher() {
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
        rcv_liveorder=view.findViewById(R.id.rcv_liveorder);
        call.enqueue(new Callback<List<RestList>>() {
            @Override
            public void onResponse(final Call<List<RestList>> call, Response<List<RestList>> response) {
                int x=0;
                restname=new String[response.body().size()];
                restid=new int[response.body().size()];
                for(RestList i : response.body())
                {
                    restname[x]=i.getRestname();
                    restid[x]=i.getRestid();
                    x++;
                }


                ArrayAdapter aa = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,restname);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                restlist.setAdapter(aa);
                restlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ls_order.clear();
                        AppSharedPreferences appSharedPreferences=new AppSharedPreferences(getContext());
                        HashMap<String,String> map=appSharedPreferences.getPref();
                        rcv_liveorder.setLayoutManager(new GridLayoutManager(getContext(), 1));
                         Toast.makeText(getContext(),restid[restlist.getSelectedItemPosition()]+"", Toast.LENGTH_SHORT).show();
                         Call<List<Liveorder>> call1=apiService.get_Live_order_peruser(restid[restlist.getSelectedItemPosition()],Integer.parseInt(map.get("userid")));
                         call1.enqueue(new Callback<List<Liveorder>>() {
                             @Override
                             public void onResponse(Call<List<Liveorder>> call, Response<List<Liveorder>> response) {
                                 for (Liveorder item : response.body()) {


                                     HashMap<String, String> map = new HashMap<>();
                                     map.put("liveid", item.getLiveid() + "");
                                     map.put("orderid", item.getOrderid() + "");
                                     map.put("tableid", item.getTableid() + "");
                                     map.put("itemid", item.getItemid() + "");
                                     map.put("itemprice", item.getItemprice() + "");
                                     map.put("userid", item.getUserid() + "");
                                     map.put("quntity", item.getQuntity() + "");
                                     map.put("itemname", item.getItemname());
                                     map.put("status", item.getStatus());
                                     map.put("order_date", item.getOrder_date().toString());

                                     ls_order.add(map);
                                 }

                                 adapter = new LiveOrderAdapter(getContext(), ls_order);
                                 rcv_liveorder.setAdapter(adapter);
                             }

                             @Override
                             public void onFailure(Call<List<Liveorder>> call, Throwable t) {

                             }
                         });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<RestList>> call, Throwable t) {
                Log.e("####",t.getMessage());
            }
        });


        return view;
    }
    private void filter(String text) {

        List<HashMap<String,String>> ls=new ArrayList<>();

        for (HashMap<String,String> s : ls_order) {
            Log.e("abcccccc",s.toString());
            if (s.toString().toLowerCase().contains(text.toLowerCase())) {
                Log.e("true", String.valueOf(s));
                ls.add(s);
            }
        }

        adapter.filterList(ls);

    }

}
