package com.example.myapplication

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    //bluetooth adapter
    lateinit var bAdapter:BluetoothAdapter
    private val REQUEST_CODE_ENABLE_BT:Int =1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bAdapter = BluetoothAdapter.getDefaultAdapter()

        val textOn: TextView = findViewById(R.id.statusBluetoothTv)
        val turnOnButton: TextView = findViewById(R.id.onBtn)
        val turnOffButton: TextView = findViewById(R.id.offBtn)
        val discoverAble: TextView = findViewById(R.id.discoverableBtn)

        if(bAdapter == null){
            textOn.text = "Bluetooth is not Found"
        }
        else{
            textOn.text = "Bluetooth is Found"
        }

        turnOnButton.setOnClickListener(){

            if(bAdapter.isEnabled){
                Toast.makeText(this, "Already on", Toast.LENGTH_LONG).show()
            }

            else{
                var intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, REQUEST_CODE_ENABLE_BT)
            }
        }

        turnOffButton.setOnClickListener()
        {
            if(!bAdapter.isEnabled){
                Toast.makeText(this, "Already off", Toast.LENGTH_LONG).show()
            }

            else{
                bAdapter.disable()
                Toast.makeText(this, "Bluetooth is off", Toast.LENGTH_LONG).show()
            }
        }

    }

}