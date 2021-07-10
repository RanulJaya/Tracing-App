package com.example.myapplication

import retrofit.Call
import retrofit.http.Body
import retrofit.http.GET
import retrofit.http.POST


public interface APIService {


    @GET("/api/test")
    open fun createUser(): Call<PhoneData>

}

