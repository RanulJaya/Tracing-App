package com.example.firstapp.ConnectBluetooth

import com.example.firstapp.Addbluetooth
import retrofit2.Call
import retrofit2.http.*


public interface APIService {


    @GET("api/test")
    fun users(): Call<List<Addbluetooth>>

    @POST("api/post")
    open fun newUser(@Body map: Addbluetooth): Call<Void>

}

