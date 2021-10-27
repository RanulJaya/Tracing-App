const data = require('./user.json')
const Excel = require('exceljs')
let workbook = new Excel.Workbook()
let worksheet = workbook.addWorksheet('Debtors')
const fs = require('fs')

const axios = require('axios')

// GET request for remote image in node.js
//add url for the url
axios({
    method: 'get',
    url: '',
}).then(function (response) {
    const data = JSON.stringify(response.data)
    // write JSON string to a file
    fs.writeFile('user.json', data, (err) => {
        if (err) {
            throw err
        }
        console.log('JSON data is saved.')
    })
})

worksheet.columns = [
    { header: 'ID', key: '_id' },
    { header: 'Name', key: 'devicename' },
    { header: 'Location', key: 'location' },
    { header: 'Start Hours', key: 'startHours' },
    { header: 'Start Minutes', key: 'startMinutes' },
    { header: 'End Minutes', key: 'endMinutes' },
    { header: 'End Hours', key: 'endHours' },
]

// force the columns to be at least as long as their header row.
// Have to take this approach because ExcelJS doesn't have an autofit property.
worksheet.columns.forEach((column) => {
    column.width = column.header.length < 12 ? 12 : column.header.length
})

// Make the header bold.
// Note: in Excel the rows are 1 based, meaning the first row is 1 instead of 0.
worksheet.getRow(1).font = { bold: true }

// Dump all the data into Excel
data.forEach((e, index) => {
    // row 1 is the header.
    // By using destructuring we can easily dump all of the data into the row without doing much
    // We can add formulas pretty easily by providing the formula property.
    worksheet.addRow({
        ...e,
    })
})

// loop through all of the rows and set the outline style.
worksheet.eachRow({ includeEmpty: false }, function (row, rowNumber) {
    worksheet.getCell(`A${rowNumber}`).border = {
        top: { style: 'thin' },
        left: { style: 'thin' },
        bottom: { style: 'thin' },
        right: { style: 'none' },
    }

    const insideColumns = ['B', 'C', 'D', 'E', 'F']
    insideColumns.forEach((v) => {
        worksheet.getCell(`${v}${rowNumber}`).border = {
            top: { style: 'thin' },
            bottom: { style: 'thin' },
            left: { style: 'none' },
            right: { style: 'none' },
        }
    })

    worksheet.getCell(`G${rowNumber}`).border = {
        top: { style: 'thin' },
        left: { style: 'none' },
        bottom: { style: 'thin' },
        right: { style: 'thin' },
    }
})

// Keep in mind that reading and writing is promise based.
workbook.xlsx.writeFile('AppTracerSpread.xlsx')
