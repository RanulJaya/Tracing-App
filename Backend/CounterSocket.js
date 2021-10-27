const express = require('express') //requires express module
const socket = require('socket.io') //requires socket.io module
const app = express()
var PORT = process.env.PORT || 3000
const server = app.listen(PORT) //tells to host server on localhost:3000

//Playing variables:
app.use(express.static('public')) //show static files in 'public' directory
console.log('Server is running')
const io = socket(server)

//Socket.io Connection------------------
io.on('connection', (socket) => {
    console.log('New socket connection: ' + socket.id)

    socket.on('send', function(message){
        socket.broadcast.emit(
            'send',message
        )
    })
})

//Socket.io Connection------------------
io.on('connection', (socket) => {
    console.log('New socket connection: ' + socket.id)

    socket.on('counter', function(test){
        socket.broadcast.emit(
            'counter',
            test
        )
    })
})
