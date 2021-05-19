let stompClient = null;
let msgArea = document.getElementById("chatlogs");
let now = null;
let hour = 0;
let min = 0;
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
function showMessageReceived(e){
	now = new Date();
	if(now.getHours() > 12){
		msgArea.innerHTML += "<div style='text-align:left; border-style:solid'><h3>"+e.sender+
					"</h3><br><h5>"+e.message+"</h5><br><h6>오후 "+now.getHours()%12+":"+now.getMinutes() +"<h6></div>";
	}else if(now.getHours() === 12){
		msgArea.innerHTML += "<div style='text-align:left; border-style:solid'><h3>"+e.sender+
					"</h3><br><h5>"+e.message+"</h5><br><h6>오후 "+now.getHours()+":"+now.getMinutes() +"<h6></div>";
	}else{
		msgArea.innerHTML += "<div style='text-align:left; border-style:solid'><h3>"+e.sender+
					"</h3><br><h5>"+e.message+"</h5><br><h6>오전 "+now.getHours()+":"+now.getMinutes() +"<h6></div>";
	}
}

function showMessageSend(e){
	now = new Date();
	if(now.getHours() > 12){
		msgArea.innerHTML += "<div style='text-align:right; border-style:solid'><h3>"+e.sender+
						"</h3><br><h5>"+e.message+"</h5><br><h6>오후 "+now.getHours()%12+":"+now.getMinutes() +"<h6></div>";
	} else if(now.getHours() === 12){
		msgArea.innerHTML += "<div style='text-align:right; border-style:solid'><h3>"+e.sender+
						"</h3><br><h5>"+e.message+"</h5><br><h6>오후 "+now.getHours()+":"+now.getMinutes() +"<h6></div>";
	}else{
		msgArea.innerHTML += "<div style='text-align:right; border-style:solid'><h3>"+e.sender+
						"</h3><br><h5>"+e.message+"</h5><br><h6>오전 "+now.getHours()+":"+now.getMinutes() +"<h6></div>";
	}
}

window.onbeforeunload = function(e){
	disconnect();
}
