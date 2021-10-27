package com.example.firstapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewActivity : AppCompatActivity() {

    var bluetoothAdapter: BluetoothAdapter? = null
    var recyclerview: RecyclerView? = null
    var pairedDevices: Set<BluetoothDevice>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        bluetoothAdapter =
            (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

        recyclerview = findViewById<RecyclerView>(R.id.recyclerview)


        recyclerview?.layoutManager = LinearLayoutManager(this)

    }

    override fun onResume() {
        super.onResume()

        val data = ArrayList<ItemsViewModel>()

        pairedDevices = bluetoothAdapter?.bondedDevices


        if (bluetoothAdapter?.isEnabled == true) {
            pairedDevices?.forEach {
                data.add(ItemsViewModel(it.name))
            }
        }

        val adapter = DynamicList(data)

        recyclerview?.adapter = adapter

    }
}