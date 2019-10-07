var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var mysql = require('mysql');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: true
}));

app.get('/', function (req, res) {
    return res.send({ error: ture, message: 'Test Employee web API' })
});

var dbConn = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'ass2_connect_mysql'
});

dbConn.connect();

app.get('/allemp', function (req,res){
    dbConn.query('SELECT * FROM employee', function (error, results, fields){
        if(error) throw error;
        return res.send(results);
    });
});

app.post('/emp', function (req,res){

    var std = req.body

    if(!std) {
        return res.status(400).send({ error:ture, message: 'Please provide employee '});
    }

    dbConn.query("INSERT INTO employee SET ?", std, function (error, results, fields){
        if (error) throw error;
        return res.send(results);
    });
});

app.listen(3000, function(){
    console.log('Node app is running on port 3000');
});

module.exports = app;