package com.example.myapplication

import retrofit.Call
import retrofit.http.Body
import retrofit.http.GET
import retrofit.http.POST


public interface APIService {

    //TODO:Needs to be further tested
    @POST("/api/postdata")
    open fun createUser(@Body user: PhoneData?): Call<PhoneData?>?

}

