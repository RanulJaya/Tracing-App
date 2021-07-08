var express = require('express');
var app = express();

const router = require('./router/api')

const log = console.log
const PORT = process.env.PORT || 8001

app.use('/api', router)

app.listen(PORT, () => {
    log(`Server is starting at PORT: ${PORT}`)
})