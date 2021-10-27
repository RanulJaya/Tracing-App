var express = require('express')
var router = express.Router()
var Addbluetooth = require('../model/bluetooth')
const axios = require('axios')

router.post('/post', async (req, res) => {
    const devicename = req.body.devicename
    const location = req.body.location
    const date = Date.now()
    let time = new Date()
    const startMinutes = req.body.startMinutes
    const startHours = req.body.startHours
    const endMinutes = time.getMinutes() 
    const endHours = time.getHours() 

    const Addnewbluetooth = new Addbluetooth({
        devicename,
        location,
        date,
        startHours,
        startMinutes,
        endMinutes,
        endHours,
    })

    console.log('test')

    Addnewbluetooth
        .save()
        .then(() => res.json('test added'))
        .catch((err) => res.status(400).json('Error: ' + err))
})

router.get('/test', async (req, res) => {
    axios.get('http://localhost:8001/api/').then((response) => {
        this.data = response.data
        var formattedUsers = []

        res.json(this.data)
    })
})

router.get('/', (req, res) => {
    Addbluetooth
        .find()
        .then((Bluetooth) => res.json(Bluetooth))
        .catch((err) =>
            res.status(404).json({ noBluetoothFound: 'No Bluetooth found' })
        )
})

module.exports = router
