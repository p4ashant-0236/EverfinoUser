package com.everfino.everfinouser.ApiConnection;


import com.everfino.everfinouser.Models.Order;
import com.everfino.everfinouser.Models.OrderItem;
import com.everfino.everfinouser.Models.User;
import com.everfino.everfinouser.Models.UserLoginResponse;
import com.google.gson.JsonObject;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface Api {

    @POST("enduser_login")
    Call<UserLoginResponse> user_login(
            @Body JsonObject object);


    @GET("rest_enduserorder/{userid}")
    Call <List<Order>> get_user_order(@Path("userid") int userid);

    @GET("rest_enduserorder/moreuserorder/{userid}")
    Call<List<OrderItem>> get_order_detail(@Path("userid") int userid, @Query("orderid") int orderid);

    @POST("enduser/add")
    Call<User> register_User(@Body JsonObject object);
}
