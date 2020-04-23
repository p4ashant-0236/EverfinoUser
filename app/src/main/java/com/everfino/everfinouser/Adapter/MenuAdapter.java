package com.everfino.everfinouser.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import com.everfino.everfinouser.ApiConnection.Api;
import com.everfino.everfinouser.ApiConnection.ApiClient;
import com.everfino.everfinouser.AppSharedPreferences;
import com.everfino.everfinouser.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.Viewholder> {

    Context context;
    List<HashMap<String,String>> ls;
    String[] colors={"#FF3300","#F5F3EF","#FFB52B","#D1EC40","#27FFBF","#CA48D9"};

    HashMap<String, String> map;
    AppSharedPreferences appSharedPreferences;
    HashMap<String,String> pref;


    public MenuAdapter(Context context, List<HashMap<String,String>> ls) {
        this.context=context;
        this.ls=ls;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.menulist_design, null);
        appSharedPreferences=new AppSharedPreferences(context);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        map=ls.get(position);
        holder.itemtype.setText(map.get("itemtype"));
        holder.itemdesc.setText(map.get("itemdesc"));
        holder.itemprice.setText(map.get("itemprice"));
        holder.itemname.setText(map.get("itemname"));
        holder.side_bar.setBackgroundColor(Color.parseColor(colors[new Random().nextInt(6)]));

    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView quantity,itemprice,itemname,itemdesc,itemtype;
        private Api apiService;
        Button inc,dec;

        LinearLayout side_bar;

        public Viewholder(@NonNull final View itemView) {
            super(itemView);
            apiService= ApiClient.getClient().create(Api.class);
            side_bar=itemView.findViewById(R.id.side_bar);
            quantity=itemView.findViewById(R.id.quantity);
            inc=itemView.findViewById(R.id.inc);
            dec=itemView.findViewById(R.id.dec);
            itemname=itemView.findViewById(R.id.txt_itemname);
            itemprice=itemView.findViewById(R.id.txt_itemprice);
            itemdesc=itemView.findViewById(R.id.txt_itemdesc);
            itemtype=itemView.findViewById(R.id.txt_itemtype);

            inc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int x=Integer.parseInt(ls.get(getAdapterPosition()).get("orderquntity"));
                    x++;
                    if(x>10)
                    {
                        x=10;
                    }
                    ls.get(getAdapterPosition()).put("orderquntity",""+x);
                    if(ls.get(getAdapterPosition()).get("orderquntity")=="0")
                    {
                        quantity.setText("");
                    }
                    quantity.setText(ls.get(getAdapterPosition()).get("orderquntity"));
                }
            });
            dec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int x=Integer.parseInt(ls.get(getAdapterPosition()).get("orderquntity"));
                    x--;
                    if(x<0)
                    {
                        x=0;
                    }
                    ls.get(getAdapterPosition()).put("orderquntity",""+x);
                    quantity.setText(ls.get(getAdapterPosition()).get("orderquntity"));
                    if(x==0)
                    {
                        quantity.setText("");
                    }
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

    public List<HashMap<String, String>> getSelected() {
        List<HashMap<String,String>> map=new ArrayList<>();
        for(HashMap<String,String> i : ls)
        {
            if(Integer.parseInt(i.get("orderquntity"))==0)
            {}
            else {
                map.add(i);
                  }
        }
        return map;
    }

    public void filterList(List<HashMap<String,String>> ls)
    {
        this.ls=ls;
        notifyDataSetChanged();
    }
}
