//express and router intitalisation
require('dotenv').config()
var express = require('express')
var app = express()
var mongoose = require('mongoose')

//intialising the port
const log = console.log
const PORT = process.env.PORT || 8001


//connect to mongodb
mongoose.connect( process.env.MONGODB_URI || 'mongodb://localhost/my_database', {
    useNewUrlParser: true,
    useUnifiedTopology: true 
});

//middleware
app.use(express.json())
const router = require('./router/api')

//production env
if (process.env.NODE_ENV === 'production') {
    app.get('/', (req, res) => {
        res.send("Back end running")
    });
}

//routes
app.use('/api', router)


//listening to port
app.listen(PORT, () => {
    log(`Server is starting at PORT: ${PORT}`)
})
