let stomp = null;
var chatPage = document.querySelector('#chat-page');
var messageForm = document.querySelector('#messageForm');
var messageinput = document.querySelector('#message');
var chatLogs = document.querySelector('#chatLogs');
const username = "userA"

function connect(event){
	let socket = new SockJS("/yuja-chat");
	stomp = Stomp.over(socket);

	stomp.connect({}, onConnected, onError);
	
	event.preventDefault();
}

function onConnected(){
	stomp.subscribe("/topic/cobong", onMessageRecieved);

	stomp.send(
		"/chat/join",
		{},
		JSON.stringify({sender:"Yohohoho", type:"JOIN"})
		)
}

function onError(error){
	chatLogs.innerHTML("에러 발생!!");
	chatLogs.style.color="red";
}

function sendMsg(){
	const msgToSend = messageInput.value.trim();
	if(msgToSend && stomp){
		const chatMsg = {
			sender: username,
			
			
		}
	}
	stomp.send("/topic/cobong")
}

function disconnection(){
	if(stomp !== null){
		stomp.disconnect();
	}
	console.log("Disconnected!");
}

