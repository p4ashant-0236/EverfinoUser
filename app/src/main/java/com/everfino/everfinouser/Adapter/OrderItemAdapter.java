package com.everfino.everfinouser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.everfino.everfinouser.ApiConnection.Api;
import com.everfino.everfinouser.ApiConnection.ApiClient;
import com.everfino.everfinouser.AppSharedPreferences;
import com.everfino.everfinouser.R;

import java.util.HashMap;
import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.Viewholder> {

    Context context;
    List<HashMap<String, String>> ls;
    HashMap<String, String> map=new HashMap<>();
    AppSharedPreferences appSharedPreferences;
    HashMap<String, String> pref;


    public OrderItemAdapter(Context context, List<HashMap<String, String>> ls) {
        this.context = context;
        this.ls = ls;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.orderitemlist_design, null);
        appSharedPreferences = new AppSharedPreferences(context);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        map = ls.get(position);
        holder.itemname.setText(map.get("itemname"));
        holder.itemdesc.setText(map.get("itemdesc"));
        holder.itemtype.setText(map.get("itemtype"));
        holder.restname.setText(map.get("restname"));
        holder.itemprice.setText(map.get("itemprice"));

    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView itemname, itemprice, restname, itemtype, itemdesc;
        private Api apiService;


        public Viewholder(@NonNull final View itemView) {
            super(itemView);
            apiService = ApiClient.getClient().create(Api.class);
            itemname = itemView.findViewById(R.id.txt_itemname);
            itemprice = itemView.findViewById(R.id.txt_itemprice);
            restname = itemView.findViewById(R.id.txt_restname);
            itemtype = itemView.findViewById(R.id.txt_itemtype);
            itemdesc = itemView.findViewById(R.id.txt_itemdesc);


        }

        public void loadFragment(Fragment fragment, View v) {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }

    public void filterList(List<HashMap<String, String>> ls) {
        this.ls = ls;
        notifyDataSetChanged();
    }

}

