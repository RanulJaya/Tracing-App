package com.example.myapplication

import android.location.Location
import androidx.annotation.BoolRes
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

data class PhoneData(val date:LocalDate, val time:LocalTime, val Range: Int, val Location:String, val PhoneNumber:String){

    //secondary constructor
    var CheckPass:Boolean = false

   public fun component():String?{
       return "Information is sent"
   }

    public fun UserInformation(phone:PhoneData):Boolean{
        //TODO: Must connect to MongoDb Realm

        //TODO: Information must be sent to MongoDb

        //TODO:When connected return the state
        CheckPass = true

        if(CheckPass){
            return CheckPass
        }
        else
        {
            return false;
        }
    }
}
