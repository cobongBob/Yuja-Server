let stomp = null;


function connect(event){
	let socket = new SockJS("/yuja");
	stomp = Stomp.over(socket);

	stomp.connect({}, onConnected, onError);
	
	event.preventDefault();
}

function onConnected(){
	stomp.subscribe("/topic/cobong", onMessageRecieved);

	stomp.send(
		"/chat/send",
		{},
		JSON.stringify({sender:"Yohohoho", type:"JOIN"})
		)
}

function disconnection(){
	if(stomp !== null){
		stomp.disconnect();
	}
	console.log("Disconnected!");
}

function sendDetail(){
	stomp.send("/topic/cobong")
}