package com.example.firstapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.firstapp.ConnectBluetooth.APIService
import com.example.firstapp.ConnectBluetooth.SocketHandler
import com.example.firstapp.ConnectBluetooth.SocketHandler.mSocket
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import com.polyak.iconswitch.IconSwitch
import io.socket.emitter.Emitter


class MainActivity : AppCompatActivity() {

    var bluetooth: BluetoothManager? = null
    var service: APIService? = null
    var iconSwitch: IconSwitch? = null
    var bottomNavigationView: BottomNavigationView? = null
    var announce: TextView? = null
    var instruct: TextView? = null
    var startMinutes: String? = null
    var startHours: String? = null
    var endMinutes: String? = null
    var endHours: String? = null
    var call: Call<List<Addbluetooth>>? = null
    var messageSent: Emitter.Listener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bluetooth =
            getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

	//add url for the base url
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(APIService::class.java)

        call = service!!.users()

        call!!.enqueue(object : Callback<List<Addbluetooth>> {
            override fun onResponse(
                call: Call<List<Addbluetooth>>,
                response: Response<List<Addbluetooth>>
            ) {
                startHours = response.body()!![1].startHours
                startMinutes = response.body()!![1].startMinutes
                endHours = response.body()!![1].endHours
                endMinutes = response.body()!![1].endMinutes

                if (startHours.toString() == "0") {
                    startHours = "24"
                }

                if (endHours.toString() == "0") {
                    endHours = "24"
                }

                val action: String? = intent.action
                val type: String? = intent.type

                if (ACTION_SEND == (action) && type != null) {
                    val builder1 = AlertDialog.Builder(this@MainActivity)
                    builder1.setMessage("The place you visited at ${response.body()!![1].location} has been compromised at ${startHours}:${startMinutes} to $endHours:${endMinutes} at ${response.body()!![1].date.toString()} send this to info other users")
                    builder1.setPositiveButton(
                        R.string.setOk,
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            mSocket.on("send", messageSent)
                            mSocket!!.emit(
                                "send",
                                "The place you visited at ${response.body()!![1].location} has been compromised at ${startHours}:${startMinutes} to $endHours:${endMinutes} at ${response.body()!![1].date.toString()} go to the closest testing station"
                            )
                        })
                    builder1.setCancelable(false)
                    builder1.create().show()
                }

            }

            override fun onFailure(call: Call<List<Addbluetooth>>, t: Throwable) {

                Toast.makeText(this@MainActivity, t.message.toString(), Toast.LENGTH_LONG)
                    .show()
            }

        })

    }


    override fun onResume() {
        super.onResume()

        // The following lines connects the Android app to the server.
        SocketHandler.setSocket()
        SocketHandler.establishConnection()
        mSocket = SocketHandler.getSocket()

        messageSent =
            Emitter.Listener { args ->
                runOnUiThread(java.lang.Runnable {
                    if (args[0] != null) {
                        val builder1 = AlertDialog.Builder(this@MainActivity)
                        builder1.setMessage(args[0].toString())
                        builder1.setCancelable(true)
                        builder1.create().show()
                    }
                })
            }


        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        iconSwitch = findViewById<IconSwitch>(R.id.iconSwitch)
        announce = findViewById(R.id.announce)
        instruct = findViewById(R.id.instruction)


        if (bluetooth?.adapter!!.isEnabled) {
            iconSwitch!!.checked = IconSwitch.Checked.LEFT
        } else {
            iconSwitch!!.checked = IconSwitch.Checked.RIGHT
        }

        iconSwitch!!.setCheckedChangeListener(IconSwitch.CheckedChangeListener {
            //simple witch case
            when (it) {
                IconSwitch.Checked.RIGHT -> {
                    bluetooth?.adapter?.disable()
                }
                IconSwitch.Checked.LEFT -> {
                    if (bluetooth?.adapter == null) {
                        Toast.makeText(this, "bluetooth does not exist", Toast.LENGTH_LONG).show()
                    }
                    if (bluetooth?.adapter?.isEnabled == false) {
                        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        startActivity(enableBtIntent)
                    } else if (bluetooth?.adapter?.isEnabled == true) {
                        Toast.makeText(this, "Bluetooth is On", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })


        bottomNavigationView!!.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            val id = item.itemId
            when (id) {
                R.id.settings -> {
                    iconSwitch!!.isVisible = false
                    announce!!.isVisible = false
                    instruct!!.isVisible = false
                    val id: String =
                        Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                    val bundle = Bundle()

                    bundle.putString("userId", id)

                    val fragObj = BlankFragment()
                    fragObj.arguments = bundle

                    val fm: FragmentManager = supportFragmentManager
                    val fragmentTransaction: FragmentTransaction = fm.beginTransaction()
                    fragmentTransaction.replace(R.id.frameLayout, fragObj)
                    fragmentTransaction.commit()

                }

                R.id.connect -> {
                    val intent = Intent(this, BluetoothConnection::class.java)
                    startActivity(intent)
                }
            }
            true
        })


    }


    override fun onPause() {
        super.onPause()
        Log.d("onPause", "onPause is called")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("onDestroy called", "onDestroy is being called")

        if (isFinishing) {
            Log.d("onFinish called", "onFinish is being called")
        }
    }

}