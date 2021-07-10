var express = require('express');
var router = express.Router();
var bluetooth = require('../model/bluetooth') 

router.post('/post', (req, res) => {

    const name = req.body.name
    const test = req.body.test

    const newbluetooth = new bluetooth({
        name,
        test
    })

    newbluetooth.save().then(() => res.json('test added'))
    .catch(err => res.status(400).json('Error: ' + err))
    
});

//FIXME: 
router.get('/test', (req, res) => {
    console.log("test")
    res.json("Test")
})

module.exports = router