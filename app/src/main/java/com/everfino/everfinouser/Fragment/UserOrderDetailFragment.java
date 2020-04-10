package com.everfino.everfinouser.Fragment;


import android.app.ProgressDialog;
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

import com.everfino.everfinouser.Adapter.OrderItemAdapter;
import com.everfino.everfinouser.ApiConnection.Api;
import com.everfino.everfinouser.ApiConnection.ApiClient;
import com.everfino.everfinouser.AppSharedPreferences;
import com.everfino.everfinouser.Models.Menu;
import com.everfino.everfinouser.Models.OrderItem;
import com.everfino.everfinouser.Models.Restaurant;
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
public class UserOrderDetailFragment extends Fragment {
    AppSharedPreferences appSharedPreferences;
    HashMap<String,String> map;
    RecyclerView rcv_order;
    ProgressDialog progressDialog;
    List<HashMap<String, String>> ls_order = new ArrayList<>();
    OrderItemAdapter adapter;
    EditText search;
    private static Api apiService;

    public UserOrderDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        final View view = inflater.inflate(R.layout.fragment_user_order_detail, container, false);
        rcv_order = view.findViewById(R.id.rcv_orderdetails);

        apiService = ApiClient.getClient().create(Api.class);
        appSharedPreferences=new AppSharedPreferences(getContext());

        adapter= new OrderItemAdapter(getContext(), ls_order);
        search=view.findViewById(R.id.searchorderdetails);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                filter(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fetch_order();
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
    private void fetch_order() {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Fetching...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        ls_order.clear();
        rcv_order.setLayoutManager(new GridLayoutManager(getContext(), 1));
        map=appSharedPreferences.getPref();
        Call<List<OrderItem>> call = apiService.get_order_detail(Integer.parseInt(map.get("userid")),getArguments().getInt("orderid"));
        call.enqueue(new Callback<List<OrderItem>>() {
            @Override
            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                for (OrderItem item : response.body()) {

                    Menu m=item.getItem();

                    Restaurant r=item.getRest();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("itemid",m.getItemid()+"");
                    map.put("itemname",m.getItemname());
                    map.put("itemprice",m.getItemprice()+"");
                    map.put("itemdesc",m.getItemdesc());
                    map.put("itemtype",m.getItemtype());
                    map.put("restname",r.getRestname());
                    ls_order.add(map);
                }

                 rcv_order.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable t) {
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
