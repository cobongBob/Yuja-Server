
let stompClient = null;
connect();

function connect(){
	let socket = new SockJS("/yuja");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(){
		stompClient.subscribe("/topic/cobong/"+username, function(e){
			showMessageReceived(JSON.parse(e.body));
		});
	});
}

function disconnect(){
	if(stompClient !== null){
		stompClient.disconnect();
	}
}

function send() {
	message = document.getElementById("message").value;
    data = {'chatRoomId': roomId, 'sender' :username, 'receiver': receiver,'message': message};
    stompClient.send("/app/chat/send", {}, JSON.stringify(data));
    showMessageSend(data);
    $("#message").val('');
}
let msgArea = document.getElementById("chatlogs");
function showMessageReceived(e){
	msgArea.innerHTML += "<div style='text-align:left; border-style:solid'><h3>"+e.sender+
					"</h3><br><h5>"+e.message+"</h5></div>";
}

function showMessageSend(e){
	msgArea.innerHTML += "<div style='text-align:right; border-style:solid'><h3>"+e.sender+
					"</h3><br><h5>"+e.message+"</h5></div>";
}

function showMessageRight(e){

}

window.onbeforeunload = function(e){
	disconnect();
}
