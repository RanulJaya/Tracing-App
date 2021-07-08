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
import java.time.LocalDate
import java.time.LocalTime


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

        bAdapter = BluetoothAdapter.getDefaultAdapter()

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
                val phone = PhoneData(date, time, 6, location, "02178324") //Dummy Data

                if(phone.UserInformation(phone)){
                    Toast.makeText(this, phone.component(), Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this, "Information did not successfully send to the database", Toast.LENGTH_LONG).show()
                }

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