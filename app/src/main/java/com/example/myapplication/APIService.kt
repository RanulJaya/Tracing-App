package com.example.myapplication

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


public interface APIService {


    @GET("/api/test")
    open fun getUserBluetooth(): Call<PhoneData>

    @POST("/api/post")
    open fun newUser(@Body map:HashMap<String, String>): Call<Void>

}

