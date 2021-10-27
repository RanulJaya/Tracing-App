package com.example.firstapp

import android.Manifest
import android.bluetooth.*
import android.content.*
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.common.api.GoogleApiClient
import java.util.*
import android.content.IntentSender.SendIntentException
import android.os.Build
import com.google.android.gms.common.api.Status
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.location.LocationListener
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import java.lang.Exception
import java.lang.reflect.Method
import android.widget.ArrayAdapter
import android.content.DialogInterface
import android.provider.Settings
import com.example.firstapp.ConnectBluetooth.APIService
import com.example.firstapp.ConnectBluetooth.SocketHandler
import com.example.firstapp.ConnectBluetooth.SocketHandler.mSocket
import io.socket.emitter.Emitter
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class BluetoothConnection : AppCompatActivity() {

    var NAME: String? = null
    var MY_UUID1: String? = null
    var bluetoothAdapter: BluetoothAdapter? = null
    var googleApi: GoogleApiClient? = null
    val interval: Long = 1000 * 60 * 1
    private val fastestInterval: Long = 1000 * 50
    var mLocationRequest: com.google.android.gms.location.LocationRequest? = null
    var builder: LocationSettingsRequest.Builder? = null
    private lateinit var fusedLocationClient: FusedLocationProviderApi
    var address: Address? = null
    private var arrayList: ArrayAdapter<DeviceItem>? = null
    private var list: ArrayAdapter<String>? = null
    val array: ArrayList<DeviceItem> = ArrayList<DeviceItem>()
    var service: APIService? = null
    var item: DeviceItem? = null
    var onNewMessage: Emitter.Listener? = null
    var startMinute: Int? = 0
    var hour: Int? = 0
    var second: Int? = 0
    var startHours: Int? = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_test)

        // The following lines connects the Android app to the server.
        SocketHandler.setSocket()
        SocketHandler.establishConnection()
        mSocket = SocketHandler.getSocket()


        bluetoothAdapter =
            (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter


        NAME = "APP"
        MY_UUID1 = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        googleApi = GoogleApiClient.Builder(this).addApi(LocationServices.API).build()
        googleApi?.connect()

        fusedLocationClient = LocationServices.FusedLocationApi


        val locationRequest: com.google.android.gms.location.LocationRequest? =
            com.google.android.gms.location.LocationRequest.create()
                .setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(interval).setFastestInterval(fastestInterval)


        mLocationRequest = com.google.android.gms.location.LocationRequest.create()
            .setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10 * 1000)
            .setFastestInterval(1 * 1000)

        builder = LocationSettingsRequest.Builder().addLocationRequest(
            locationRequest!!
        )
        builder?.setAlwaysShow(true)
    }


    override fun onResume() {
        super.onResume()

        val button: Button = findViewById(R.id.btnDiscoverable)
        val location: Button = findViewById(R.id.locationPhone)
        val view: Button = findViewById(R.id.recycler)
        val discoverable: Button = findViewById(R.id.discoverDevice)


        // The following lines connects the Android app to the server.
        SocketHandler.setSocket()
        SocketHandler.establishConnection()
        mSocket = SocketHandler.getSocket()

        mSocket!!.emit("counter", MY_UUID1.toString())


        onNewMessage =
            Emitter.Listener { args ->
                runOnUiThread(java.lang.Runnable {
                    if (args[0] != null) {
                        if (args[0].toString() != MY_UUID1.toString()) {
			//add url for the base url
                            val retrofit: Retrofit = Retrofit.Builder()
                                .baseUrl("")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()

                            service = retrofit.create(APIService::class.java)

                            val deviceInfo: Addbluetooth =
                                Addbluetooth(
                                    MY_UUID1.toString(),
                                    item!!.getAddress().toString(),
                                    "",
                                    startHours.toString(),
                                    startMinute.toString(),
                                    "",
                                    ""
                                )

                            val call = service!!.newUser(deviceInfo)

                            call.enqueue(object : Callback<Void> {
                                override fun onResponse(
                                    call: Call<Void>,
                                    response: Response<Void>
                                ) {
                                    Toast.makeText(
                                        this@BluetoothConnection,
                                        "successful",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {

                                    Toast.makeText(
                                        this@BluetoothConnection,
                                        "not working",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            })

                            val deviceInfo1: Addbluetooth =
                                Addbluetooth(
                                    args[0].toString(), item!!.getAddress().toString(), "",
                                    startHours.toString(),
                                    startMinute.toString(),
                                    "",
                                    ""
                                )

                            val call1 = service!!.newUser(deviceInfo1)

                            call1.enqueue(object : Callback<Void> {
                                override fun onResponse(
                                    call: Call<Void>,
                                    response: Response<Void>
                                ) {

                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {

                                }
                            })
                        }
                    }
                })
            }




        button.setOnClickListener(View.OnClickListener {

            // Register for broadcasts when a device is discovered.
            if (bluetoothAdapter?.isDiscovering == true) {

                bluetoothAdapter?.cancelDiscovery()
                bluetoothAdapter?.startDiscovery()

                checkBTPermissions()

                val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
                filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
                filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED)
                filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)

                registerReceiver(receiver, filter)
            }


            if (bluetoothAdapter?.isDiscovering == false) {

                checkBTPermissions()

                bluetoothAdapter?.startDiscovery()
                val discoverDevicesIntent =
                    IntentFilter(BluetoothDevice.ACTION_FOUND)
                registerReceiver(receiver, discoverDevicesIntent)
            }

            val alert: AlertDialog.Builder = AlertDialog.Builder(this@BluetoothConnection)
            alert.setTitle("Bluetooth Device(s)")

            arrayList = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line)
            list = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line)

            alert.setAdapter(
                list,
                object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        createBond(arrayList!!.getItem(p1)!!.getConnected())
                        item = arrayList!!.getItem(p1)

                    }
                })
            alert.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    p0?.cancel()
                }

            })
            alert.show()
        })



        location.setOnClickListener(View.OnClickListener {
            val result =
                LocationServices.SettingsApi.checkLocationSettings(googleApi, builder?.build())
            result.setResultCallback { result ->
                val status: Status = result.status
                when (status.statusCode) {
                    LocationSettingsStatusCodes.SUCCESS -> {
                        getLocation()

                        Toast.makeText(
                            this@BluetoothConnection,
                            "Location is on",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.i(
                            TAG,
                            "Location settings are not satisfied. Show the user a dialog to upgrade location settings "
                        )
                        try {
                            status.startResolutionForResult(
                                this@BluetoothConnection,
                                1
                            )
                        } catch (e: SendIntentException) {
                            Log.i(TAG, "PendingIntent unable to execute request.")
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->
                        Toast.makeText(
                            this@BluetoothConnection,
                            "Location is not Available",
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }

        })

        view.setOnClickListener(View.OnClickListener {
            val result =
                LocationServices.SettingsApi.checkLocationSettings(googleApi, builder?.build())
            result.setResultCallback { result ->
                val status: Status = result.status

                if (status.statusCode != LocationSettingsStatusCodes.SUCCESS) {
                    Toast.makeText(this@BluetoothConnection, "Turn location on", Toast.LENGTH_LONG)
                        .show()
                } else {
                    val intent = Intent(this, ViewActivity::class.java)
                    intent.putParcelableArrayListExtra("listView", array)
                    startActivity(intent)
                }
            }
        })

        discoverable.setOnClickListener(View.OnClickListener {
            makeDiscoverable()
        })
    }

    private fun getLocation(): Address? {

        var locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        var latitude: Double? = null
        var longitude: Double? = null
        var locationListener = object : LocationListener {
            override fun onLocationChanged(p0: Location) {
                latitude = p0.latitude
                longitude = p0.longitude

                Log.i("testlocation", "Latitute: $latitude ; Longitute: $longitude")

                val geocoder = Geocoder(this@BluetoothConnection, Locale.getDefault())
                var addresses: List<Address?>? = null

                try {
                    addresses = geocoder.getFromLocation(
                        latitude!!,
                        longitude!!,
                        1
                    )
                } catch (ioException: Exception) {
                    Log.e("failedToFindLocation", "Error in getting address for the location")
                }

                address = addresses?.get(0)

            }

        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION
            )

        }

        val location = locationManager!!.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0L,
            0f,
            locationListener
        )

        return address

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> getLocation()
                PackageManager.PERMISSION_DENIED -> {
                    Toast.makeText(
                        this@BluetoothConnection,
                        "Location needs to be on",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }


    private fun checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            var permissionCheck = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION")
            } else {
                TODO("VERSION.SDK_INT < M")
            }
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION")
            if (permissionCheck != 0) {

                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 1001
                )
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.")
        }
    }

    private fun makeDiscoverable() {
        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        startActivity(discoverableIntent)
        Log.i("Log", "Discoverable ")
    }


    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            // Discovery has found a device. Get the BluetoothDevice
            // object and its info from the Intent.
            val device: BluetoothDevice? =
                intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
            val deviceName = device?.name
            val deviceHardwareAddress = device?.address // MAC address

            if (BluetoothDevice.ACTION_FOUND == action && deviceName != null && getLocation() != null) {
                val deviceItem = DeviceItem(
                    deviceName,
                    address!!.subLocality.toString() + ", " + address!!.locality.toString(),
                    device
                )

                if (array!!.size > 0) {
                    for (i in 0 until array!!.size) {
                        for (j in 0 until array!!.size) {
                            if (array!![j].getDeviceName() != deviceItem.getDeviceName()) {
                                array.add(deviceItem)
                                arrayList?.add(deviceItem)
                                list?.add(deviceName)
                                break
                            }
                        }
                    }

                } else {
                    array.add(deviceItem)
                    arrayList?.add(deviceItem)
                    list?.add(deviceName)
                }
            }

            if (BluetoothDevice.ACTION_ACL_CONNECTED == action) {

                val calendar = Calendar.getInstance()
                startHours = calendar[Calendar.HOUR_OF_DAY]
                hour = calendar[Calendar.HOUR]
                startMinute = calendar[Calendar.MINUTE]
                second = calendar[Calendar.SECOND]

            }

            if (BluetoothDevice.ACTION_ACL_DISCONNECTED == action) {

                val calendar = Calendar.getInstance()
                val hour24hrs = calendar[Calendar.HOUR_OF_DAY]
                val hour12hrs = calendar[Calendar.HOUR]
                val minutes = calendar[Calendar.MINUTE]
                val seconds = calendar[Calendar.SECOND]

                if (minutes - startMinute!! < 5) {
                    removeBond(device)
                    Toast.makeText(this@BluetoothConnection, "Pairing Failed", Toast.LENGTH_LONG)
                        .show()
                } else if (minutes - startMinute!! > 5 || hour12hrs - hour!! == 1 || hour12hrs - hour!! == -1) {
                mSocket.on("counter", onNewMessage)
                mSocket.connect()
                }

            }
        }
    }

    @Throws(Exception::class)
    fun createBond(btDevice: BluetoothDevice?): Boolean {
        val class1 = Class.forName("android.bluetooth.BluetoothDevice")
        val createBondMethod: Method = class1.getMethod("createBond")


        return createBondMethod.invoke(btDevice) as Boolean
    }

    @Throws(Exception::class)
    fun removeBond(btDevice: BluetoothDevice?): Boolean {
        val class1 = Class.forName("android.bluetooth.BluetoothDevice")
        val removeBondMethod = class1.getMethod("removeBond")
        return removeBondMethod.invoke(btDevice) as Boolean
    }
}


