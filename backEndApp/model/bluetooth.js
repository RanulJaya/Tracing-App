const mongoose = require('mongoose')
const Schema = mongoose.Schema

const bluetooth = new Schema ({
    name: {type:String},
    test: {type:String}
})

const newbluetooth = mongoose.model('userbluetooth', bluetooth)
module.exports = newbluetooth