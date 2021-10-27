const mongoose = require('mongoose')
const Schema = mongoose.Schema

const Addbluetooth = new Schema ({
    devicename: {type:String},
    location: {type:String},
    date: {type:Date},
    startHours:{type:Number},
    startMinutes:{type: Number},
    endHours:{type:Number},
    endMinutes:{type: Number}
})

const newbluetooth = mongoose.model('Addbluetooth', Addbluetooth)
module.exports = newbluetooth