package com.everfino.everfinouser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.everfino.everfinouser.ApiConnection.Api;
import com.everfino.everfinouser.ApiConnection.ApiClient;
import com.everfino.everfinouser.Models.User;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUserActivity extends AppCompatActivity {
    EditText name,password,mobileno,dob,email;
    Spinner gender;
    Button signup;
    private static Api apiService;
    String g[]={"Male","Female"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        name=findViewById(R.id.name);
        password=findViewById(R.id.password);
        mobileno=findViewById(R.id.mobileno);
        dob=findViewById(R.id.dob);
        email=findViewById(R.id.email);
        signup=findViewById(R.id.btn_register);
        gender=findViewById(R.id.gender);
        apiService= ApiClient.getClient().create(Api.class);
        ArrayAdapter<String>  data=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,g);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(data);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("##33","enter");
                JsonObject ob=new JsonObject();
                ob.addProperty("name",name.getText().toString());
                ob.addProperty("email",email.getText().toString());
                ob.addProperty("dob",dob.getText().toString());
                ob.addProperty("mobileno",mobileno.getText().toString());
                ob.addProperty("password",password.getText().toString());
                ob.addProperty("gender",gender.getSelectedItem().toString());
                ob.addProperty("status","Activate");

                Call<User> call=apiService.register_User(ob);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.body()!=null) {
                            if(response.body().getUserid()!=0) {
                                Log.e("##33","enter");
                                Toast.makeText(RegisterUserActivity.this, "Success try to login", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RegisterUserActivity.this, LoginActivity.class);
                                startActivity(i);
                            }else{Log.e("333333","dfsfd");}
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("#################",t.getMessage());
                    }
                });
            }
        });
    }

}
