package com.everfino.everfinouser.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.txtdemo.setText(map.get("restname")+map.get("status"));

    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView txtdemo;
        private Api apiService;


        public Viewholder(@NonNull final View itemView) {
            super(itemView);
            apiService= ApiClient.getClient().create(Api.class);
            txtdemo=itemView.findViewById(R.id.txtdemo);
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
                    b.putString("restid", ls.get(getAdapterPosition()).get("restid"));
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

    }

}
