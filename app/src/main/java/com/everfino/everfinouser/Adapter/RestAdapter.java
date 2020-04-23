package com.everfino.everfinouser.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.everfino.everfinouser.Fragment.DisplayRestMenuFragment;
import com.everfino.everfinouser.R;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RestAdapter extends RecyclerView.Adapter<RestAdapter.Viewholder> {

    Context context;
    List<HashMap<String,String>> ls;
    HashMap<String, String> map;


    public RestAdapter(Context context, List<HashMap<String,String>> ls) {
        this.context=context;
        this.ls=ls;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.restlist_design, null);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        map=ls.get(position);
        if(map.get("status").equals("Activate")){
            holder.side_bar.setBackgroundColor(Color.parseColor("#008900"));
        }else {
            holder.side_bar.setBackgroundColor(Color.parseColor("#FF0000"));
        }
        holder.restname.setText(map.get("restname"));
        holder.restdesc.setText(map.get("restdesc"));
        holder.email.setText(map.get("email"));
        holder.mobileno.setText(map.get("mobileno"));
        holder.city.setText(map.get("city"));


    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView restname,restdesc,email,mobileno,city;
        LinearLayout side_bar;
        private Api apiService;


        public Viewholder(@NonNull final View itemView) {
            super(itemView);
            apiService= ApiClient.getClient().create(Api.class);
            side_bar=itemView.findViewById(R.id.side_bar);
            restname=itemView.findViewById(R.id.txt_restname);
            restdesc=itemView.findViewById(R.id.txt_restdesc);
            email=itemView.findViewById(R.id.txt_restemail);
            mobileno=itemView.findViewById(R.id.txt_restmobile);
            city=itemView.findViewById(R.id.txt_restcity);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment=new DisplayRestMenuFragment();
                    Bundle b=new Bundle();
                    if(ls.get(getAdapterPosition()).get("status").equals("Activate")) {

                        b.putString("restid", ls.get(getAdapterPosition()).get("restid"));
                        fragment.setArguments(b);
                        loadFragment(fragment, itemView);
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
    public void filterList(List<HashMap<String,String>> ls)
    {
        this.ls=ls;

    }

}
