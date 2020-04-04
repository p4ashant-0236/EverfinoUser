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

import com.everfino.everfinouser.Adapter.OrderAdapter;
import com.everfino.everfinouser.ApiConnection.Api;
import com.everfino.everfinouser.ApiConnection.ApiClient;
import com.everfino.everfinouser.AppSharedPreferences;
import com.everfino.everfinouser.LoginActivity;
import com.everfino.everfinouser.Models.Order;
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
public class HistoryFragment extends Fragment {


    AppSharedPreferences appSharedPreferences;
    HashMap<String,String> map;
    OrderAdapter adapter;

    RecyclerView rcv_order;
    EditText search;
    ProgressDialog progressDialog;
    List<HashMap<String, String>> ls_order = new ArrayList<>();
    private static Api apiService;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        final View view = inflater.inflate(R.layout.fragment_history, container, false);
        rcv_order = view.findViewById(R.id.rcv_order);

        apiService = ApiClient.getClient().create(Api.class);
        appSharedPreferences=new AppSharedPreferences(getContext());

        search=view.findViewById(R.id.searchuserorder);
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
        adapter = new OrderAdapter(getContext(), ls_order);

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
        Call<List<Order>> call = apiService.get_user_order(Integer.parseInt(map.get("userid")));
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                for (Order item : response.body()) {

                    HashMap<String, String> map = new HashMap<>();
                    map.put("orderid", item.getOrderid() + "");
                    map.put("userid",item.getUserid()+"");
                    map.put("amount",item.getAmount()+"");
                    map.put("paymentstatus",item.getPaymentstatus());
                    map.put("order_date",item.getOrder_date()+"");

                    ls_order.add(map);
                }

                 rcv_order.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
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
