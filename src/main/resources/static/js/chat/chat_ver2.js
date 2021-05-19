

function connect(){
	const socket = new SockJS("/yuja");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(){
		stompClient.subscribe("/topic/cobong/"+nickname, function(e){
			showMessage(JSON.parse(e.body));
			alertClosing('comeMessage', 2000);
		});
	});
}

function send() {
    data = {'chatRoomId': chatRoomId, 'sender' :nickname, 'receiver': receiver,'message': $("#message").val()};
    stompClient.send("/app/chat/send", {}, JSON.stringify(data));
    showMessage(data);
    $("#message").val('');
    alertClosing('successMessage',2000);
}