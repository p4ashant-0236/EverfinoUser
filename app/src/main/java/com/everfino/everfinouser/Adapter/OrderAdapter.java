package com.everfino.everfinouser.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.everfino.everfinouser.ApiConnection.Api;
import com.everfino.everfinouser.ApiConnection.ApiClient;
import com.everfino.everfinouser.AppSharedPreferences;
import com.everfino.everfinouser.Fragment.UserOrderDetailFragment;
import com.everfino.everfinouser.R;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Viewholder> {

    Context context;
    List<HashMap<String,String>> ls;
    HashMap<String, String> map;
    AppSharedPreferences appSharedPreferences;
    HashMap<String,String> pref;
    String[] colors={"#FF3300","#F5F3EF","#FFB52B","#D1EC40","#27FFBF","#CA48D9"};

    public OrderAdapter(Context context, List<HashMap<String,String>> ls) {
        this.context=context;
        this.ls=ls;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.orderlist_design, null);
        appSharedPreferences=new AppSharedPreferences(context);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        map=ls.get(position);

        holder.side_bar.setBackgroundColor(Color.parseColor(colors[new Random().nextInt(6)]));
        holder.orderid.setText(map.get("orderid"));
        holder.amount.setText(map.get("amount"));
        holder.paymentstatus.setText(map.get("paymentstatus"));
        holder.order_date.setText(map.get("order_date"));

    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView orderid,amount,paymentstatus,order_date;
        private  Api apiService;
        LinearLayout side_bar;


        public Viewholder(@NonNull final View itemView) {
            super(itemView);
            apiService= ApiClient.getClient().create(Api.class);
            orderid=itemView.findViewById(R.id.txt_orderid);
            amount=itemView.findViewById(R.id.txt_amount);
            paymentstatus=itemView.findViewById(R.id.txt_paymentstatus);
            order_date=itemView.findViewById(R.id.txt_order_date);
            side_bar=itemView.findViewById(R.id.side_bar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Fragment fragment=new UserOrderDetailFragment();
                    Bundle b=new Bundle();
                    b.putInt("orderid",Integer.parseInt(ls.get(getAdapterPosition()).get("orderid")));

                    fragment.setArguments(b);

                    loadFragment(fragment,itemView);
                }
            });

        }

        public void loadFragment(Fragment fragment,View v) {
            AppCompatActivity activity=(AppCompatActivity) v.getContext();
            FragmentTransaction transaction =activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }

    public void filterList(List<HashMap<String,String>> ls)
    {
        this.ls=ls;
        notifyDataSetChanged();
    }
}

