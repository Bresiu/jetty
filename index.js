var WebSocketServer = require('websocket').server;
var http = require('http');
var id = 0;
var port = 1337;

var server = http.createServer(function(request, response) {
    // process HTTP request. Since we're writing just WebSockets server
    // we don't have to implement anything.
});

server.listen(port, function() { });
console.log('listening on port: ' + port)
// create the server
wsServer = new WebSocketServer({
    httpServer: server
});

// WebSocket server
wsServer.on('request', function(request) {
    var connection = request.accept(null, request.origin);
	console.log('connected new client: ' + id);
	id++;

    // This is the most important callback for us, we'll handle
    // all messages from users here.
    connection.on('message', function(message) {
        if (message.type === 'utf8') {
            console.log(message.utf8Data);
            connection.send(message.utf8Data);
        }
    });

    connection.on('close', function(connection) {
        console.log('disconnected');
    });

    // Log errors
	connection.onerror = function (error) {
  		console.log('WebSocket Error ' + error);
	};
});