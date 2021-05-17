let stomp = null;
const chatPage = document.querySelector('#chat-page');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const chatLogs = document.querySelector('#chatLogs');
//const username = sessionStorage.userData.nickname;
const username = "user";

let socket = new SockJS("/yuja");
stomp = Stomp.over(socket);

stomp.connect({}, onConnected, onError);

function onConnected(){
	stomp.subscribe("/topic/cobong", onMessageReceived);

	stomp.send(
		"/app/chat/join",
		{},
		JSON.stringify({sender:username, type:"JOIN"})
		)
}

function onError(error){
	chatLogs.innerHTML("에러 발생!!");
	chatLogs.style.color="red";
}

function sendMsg(event){
	event.preventDefault();
	const msgToSend = messageInput.value.trim();
	if(msgToSend && stomp){
		const chatMsg = {
			sender: username,
			content: messageInput.value+"\n",
			type: 'CHAT'
		};
		stomp.send("/app/chat/send", {}, JSON.stringify(chatMsg));
		messageInput.value= '';
	}
}

function onMessageReceived(payload){
	const msg = JSON.parse(payload.body);
	
	if(msg.type === "JOIN"){
		msg.content = msg.sender + "님이 들어왔습니다.\n";
	} else if(msg.type === "LEFT"){
		msg.content = msg.sender + "님이 나가셨습니다.\n";
	} 
	chatLogs.append(msg.sender +" : "+msg.content)
}

function disconnection(){
	if(stomp !== null){
		stomp.disconnect();
	}
	console.log("Disconnected!");
}

messageForm.addEventListener('submit', sendMsg, true)