package com.example.walkaloop.network;


import com.example.walkaloop.model.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

//    @POST("save")
//    Call<String> insertMarker (@Body Response response);
//
    @GET("GetByPlace")
    Call<Response> getallDetails(@Query("Place") String place);
}
