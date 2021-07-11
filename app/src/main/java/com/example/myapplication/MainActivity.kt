package com.example.myapplication

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    //bluetooth adapter
    lateinit var bAdapter: BluetoothAdapter
    private val REQUEST_CODE_ENABLE_BT: Int = 1
    private var lstvw: ListView? = null
    private var aAdapter: ArrayAdapter<*>? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textOn: TextView = findViewById(R.id.statusBluetoothTv)
        val turnOnButton: TextView = findViewById(R.id.onBtn)
        val turnOffButton: TextView = findViewById(R.id.offBtn)
        val btn: TextView = findViewById(R.id.pairedBtn)
        val connectBtn: TextView = findViewById(R.id.conenctbtn)

        val BASE_URL:String = "https://host-backend.herokuapp.com/"
        var phoneUser:List<PhoneData>  ?= null

        bAdapter = BluetoothAdapter.getDefaultAdapter()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APIService::class.java)

        if (bAdapter.isEnabled) {
            textOn.text = "Bluetooth is Found"
        } else {
            textOn.text = "Bluetooth is not Found"
        }

        connectBtn.setOnClickListener(){

            if(bAdapter.isEnabled) {
                val date = LocalDate.now()
                val time = LocalTime.now()
                val location = LOCATION_SERVICE
                 //Dummy Data

                val map: HashMap<String, String> = HashMap()

                map["name"]= "Hello"
                map["test"] = "Goodbye"

                val call = service.newUser(map)

                //TODO:gson needs to convert to json
                call.enqueue(object : Callback<Void>{
                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        if(response?.code() ==200){
                            Toast.makeText(this@MainActivity, "this works", Toast.LENGTH_LONG).show()
                        }

                    }

                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        Toast.makeText(this@MainActivity, t?.message, Toast.LENGTH_LONG).show()
                    }

                })
            }

            else{
                Toast.makeText(this, "Bluetooth is not On", Toast.LENGTH_LONG).show()

            }
        }



        turnOnButton.setOnClickListener {


            if (bAdapter.isEnabled) {
                Toast.makeText(this, "Already on", Toast.LENGTH_LONG).show()
            } else {
                var intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, REQUEST_CODE_ENABLE_BT)
                textOn.text = "Bluetooth is Found"
            }
        }

        turnOffButton.setOnClickListener()
        {
            if (!bAdapter.isEnabled) {
                Toast.makeText(this, "Already off", Toast.LENGTH_LONG).show()
            } else {
                bAdapter.disable()
                Toast.makeText(this, "Bluetooth is off", Toast.LENGTH_LONG).show()

                textOn.text = "Bluetooth is not Found"
            }
        }
        btn.setOnClickListener {
            if (!(bAdapter.isEnabled)) {
                Toast.makeText(
                    applicationContext,
                    "Bluetooth is not On",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                Toast.makeText(
                    applicationContext,
                    "Bluetooth Supported",
                    Toast.LENGTH_SHORT
                ).show()

                val pairedDevices = bAdapter.bondedDevices
                val list = ArrayList<Any>()
                if (pairedDevices.size > 0) {
                    for (device in pairedDevices) {
                        val devicename = device.name
                        val macAddress = device.address
                        list.add("Name: " + devicename + "MAC Address: " + macAddress)

                    }

                    lstvw = findViewById<View>(R.id.deviceList) as ListView

                    aAdapter =
                        ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, list)
                    lstvw!!.adapter = aAdapter
                }
            }
        }
    }
}