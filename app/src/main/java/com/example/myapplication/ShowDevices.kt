package com.example.myapplication

class ShowDevices(number: Int, s: String) {

    var user:Int = 0;

    constructor(number: Int) : this(3,"") {
        this.user = number
    }

    public fun test():String{
        return user.toString()
    }
}