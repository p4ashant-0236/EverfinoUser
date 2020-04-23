package com.everfino.everfinouser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.everfino.everfinouser.ApiConnection.Api;
import com.everfino.everfinouser.ApiConnection.ApiClient;
import com.everfino.everfinouser.Models.Mail;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotActivity extends AppCompatActivity {
    EditText email,mobile;
    Button submit;
    private static Api apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        email=findViewById(R.id.email);
        mobile=findViewById(R.id.mobileno);
        submit=findViewById(R.id.sendemail);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService= ApiClient.getClient().create(Api.class);
                JsonObject object=new JsonObject();
                object.addProperty("email",email.getText().toString());
                object.addProperty("mobileno",mobile.getText().toString());
                Call<Mail> call=apiService.forgot(object);
                call.enqueue(new Callback<Mail>() {
                    @Override
                    public void onResponse(Call<Mail> call, Response<Mail> response) {
                        if(response.body().getStatus())
                        {
                        Toast.makeText(ForgotActivity.this, "Mail send", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(ForgotActivity.this,LoginActivity.class);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onFailure(Call<Mail> call, Throwable t) {

                    }
                });
            }
        });
    }
}
