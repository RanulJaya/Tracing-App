package com.example.myapplication

import android.widget.Toast
import retrofit.Call
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import java.time.LocalDate
import java.time.LocalTime

data class PhoneData(val date:LocalDate, val time:LocalTime, val Range: Int, val Location:String, val PhoneNumber:String){

    var success:Boolean = false
    private val BASE_URL = "http://localhost:8001"
    var phoneUser:List<PhoneData>  ?= null
    private var service:APIService ?= null

   public fun component():String?{
       return "Information is sent"
   }

    public fun UserInformation(data:PhoneData):Boolean{

        var retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).
            baseUrl(BASE_URL).build()
        service = retrofit.create(APIService::class.java)

        success = true
        //TODO: Testing variable for search function : I/System.out:

        if(success){
            return success
        }
        else
        {
            return success;
        }
    }
}
