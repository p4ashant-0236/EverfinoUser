package com.everfino.everfinouser;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class AppSharedPreferences {
     SharedPreferences sharedPreferences;
     Context context;

     int userid;
     String email;

     final String prefname="EVERFINO_PREF";
     public AppSharedPreferences(Context context)
     {  this.context=context;
         sharedPreferences=context.getSharedPreferences(prefname,Context.MODE_PRIVATE);
     }

     public void setPref(int userid,String email)
     {
         this.userid=userid;
         this.email=email;

         sharedPreferences.edit().putInt("userid",this.userid).putString("email",this.email).commit();
     }

     public HashMap<String,String> getPref()
     {  HashMap<String,String> map=new HashMap<>();
         map.put("userid",sharedPreferences.getInt("userid",0)+"");
         map.put("email",sharedPreferences.getString("email",""));

         return map;
     }
     public  void clearPref()
     {
         sharedPreferences.edit().clear().commit();
     }
}
