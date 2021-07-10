package com.example.myapplication

import retrofit.Call
import retrofit.http.Body
import retrofit.http.GET
import retrofit.http.POST


public interface APIService {

    @GET("/api/test")
    open fun getUserBluetooth(): Call<PhoneData>

    @POST("/api/post")
    open fun newUser(@Body map:HashMap<String, String>): Call<Void>

}

