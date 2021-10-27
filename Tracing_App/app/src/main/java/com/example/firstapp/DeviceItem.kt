package com.example.firstapp

import android.bluetooth.BluetoothDevice
import android.os.Parcel
import android.os.Parcelable

class DeviceItem() :Parcelable {

    private var deviceName: String? = null
    private var address: String? = null
    private var device:BluetoothDevice? = null

    constructor(parcel: Parcel) : this() {
        deviceName = parcel.readString()
        address = parcel.readString()
        device = parcel.readParcelable(BluetoothDevice::class.java.classLoader)
    }

    fun getDeviceName(): String? {
        return deviceName
    }

    fun getConnected(): BluetoothDevice? {
        return device
    }

    fun getAddress(): String? {
        return address
    }

    fun setDeviceName(deviceName: String?) {
        this.deviceName = deviceName
    }

    constructor(name: String?, address: String?, connected: BluetoothDevice?) : this() {
        deviceName = name
        this.address = address
        this.device  = connected
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(deviceName)
        parcel.writeString(address)
        parcel.writeParcelable(device, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DeviceItem> {
        override fun createFromParcel(parcel: Parcel): DeviceItem {
            return DeviceItem(parcel)
        }

        override fun newArray(size: Int): Array<DeviceItem?> {
            return arrayOfNulls(size)
        }
    }


}