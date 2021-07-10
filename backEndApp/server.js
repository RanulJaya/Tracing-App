//express and router intitalisation
var express = require('express')

var app = express()
app.use(express.json())

const router = require('./router/api')

//intialising the port
const log = console.log
const PORT = process.env.PORT || 8001


//TODO:middleware


//TODO: connect to mongodb


//TODO:production env


//TODO:routes
app.use('/api', router)

app.listen(PORT, () => {
    log(`Server is starting at PORT: ${PORT}`)
})
