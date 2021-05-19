

function connect(){
	const socket = new SockJS("/yuja");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(){
		stompClient.subscribe("/topic/cobong/"+nickname, function(e){
			showMessage(JSON.parse(e.body));
		});
	});
}

function disconnect(){
	if(stompClient !== null){
		stompClient.disconnect();
	}
}

function send() {
    data = {'chatRoomId': roomId, 'sender' :nickname, 'receiver': receiver,'message': $("#message").val()};
    stompClient.send("/app/chat/send", {}, JSON.stringify(data));
    showMessage(data);
    $("#message").val('');
}

function showMessage(e){
	msgArea = document.getElementById("msgArea");
	msgArea.innerHTML = "<div class='row'> <div class='col-lg-12'> <div class='media'> <div class='media-body'> <h4 class='media-heading'>" +
		e.sender + "</h4><h4 class='small pull-right'>방금</h4> </div> <p>" +
		e.message + "</p> </div> </div> </div> <hr>" + space.innerHTML;
}

window.onbeforeunload = function(e){
	disconnect();
}
