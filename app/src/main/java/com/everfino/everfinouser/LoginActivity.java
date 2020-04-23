package com.everfino.everfinouser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.everfino.everfinouser.ApiConnection.Api;
import com.everfino.everfinouser.ApiConnection.ApiClient;
import com.everfino.everfinouser.Models.UserLoginResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edt_usernme, edt_password;
    Button btn_login;
    TextView signup,Forgot;
    ProgressDialog progressDialog;
    private static Api apiService;
    AppSharedPreferences appSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_usernme = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        signup = findViewById(R.id.signup);
        btn_login = findViewById(R.id.btn_login);

        Forgot=findViewById(R.id.Forgot);
        Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(LoginActivity.this,ForgotActivity.class);
                startActivity(i);
            }
        });

        apiService = ApiClient.getClient().create(Api.class);
        appSharedPreferences = new AppSharedPreferences(this);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterUserActivity.class);
                startActivity(i);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(true);
                progressDialog.setMessage("Validating...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                user_login();
            }
        });
    }

    public void user_login() {
        if (edt_usernme.getText().length() == 0) {
            edt_usernme.setError("Username is Required!");
        } else if (edt_password.getText().length() == 0) {
            edt_password.setError("Password is Required!");
        } else {
            JsonObject inputData = new JsonObject();
            inputData.addProperty("username", edt_usernme.getText().toString());
            inputData.addProperty("password", edt_password.getText().toString());

            Call<UserLoginResponse> call = apiService.user_login(inputData);
            call.enqueue(new Callback<UserLoginResponse>() {
                @Override
                public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                    progressDialog.dismiss();
                    Log.e("####res", response.body().getStatus() + "");
                    if (response.body().getStatus() == true) {
                        appSharedPreferences.setPref(response.body().getUserid(), response.body().getEmail());
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "check username and password", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("####err", t.toString());
                }
            });
        }
    }
}
