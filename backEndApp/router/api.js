var express = require('express');
var router = express.Router();


router.post('/postdata', (req, res) => {
    
    res.send('POST request to the homepage')
    if(res.status(200)){
        console.log("It is working")
    }
});

router.get('/test', (req, res) => {
    console.log("test")
    res.json("Hello")
})

module.exports = router