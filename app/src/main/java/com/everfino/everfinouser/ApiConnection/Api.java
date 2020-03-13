package com.everfino.everfinouser.ApiConnection;


import com.everfino.everfinouser.Models.UserLoginResponse;
import com.google.gson.JsonObject;


import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.POST;


public interface Api {

    @POST("enduser_login")
    Call<UserLoginResponse> user_login(
            @Body JsonObject object);




}
