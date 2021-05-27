let stompClient = null;
let msgArea = document.getElementById('chatLog');
let now = null;
let hour = 0;
let min = 0;
connect();


function connect() {
  let socket = new SockJS('/yuja');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function () {
    stompClient.subscribe('/topic/cobong/' + username, function (e) {
      showMessageReceived(JSON.parse(e.body));
    });
  });
  window.scrollTo(0, document.body.scrollHeight);
}

function disconnect() {
  if (stompClient !== null) {
    stompClient.disconnect();
    location.href = "/rooms";
  }
}

function send() {
  message = document.getElementById('message').value;
  if(message === null || message === ""){
	return false;
	}
  if(message.includes("&")){
	message = message.replaceAll("&", "&amp");
	}
  if(message.includes("<") || message.includes(">") || message.includes("'") || message.includes('"')){
	console.log("visited");
	message = message.replaceAll('<', '&lt');
	message = message.replaceAll(">", "&gt");
	message = message.replaceAll("'", "&#39");
	message = message.replaceAll('"', "&quot");
	}
  data = {
    chatRoomId: roomId,
    sender: username,
    receiver: receiver,
    message: message,
  };
  stompClient.send('/app/chat/send', {}, JSON.stringify(data));
  showMessageSend(data);
  $('#message').val('');
  return false;
}

// 채팅 진행중 -> 메세지 받을때 보여지는 div
function showMessageReceived(e) {
  now = new Date();
  if (now.getHours() > 12) {
    msgArea.innerHTML +=
      "<div class='ChatReceiverBigWrapper'>" +
      "<div class='ChatReceiverWrapper'>" +
      "<div class='ReceiverImgWrapper'>" +
      "<img class='ChatReceiverProfileImg' src='" +
      receiverPic +
      "'>" +
      '</div>' +
      "<div class='ChatMessageReceiver'>" +
      e.sender +
      '</div>' +
      "<div class='ReceiverChatMessageContent'>" +
      "<p class='ChatContent'>" +
      e.message +
      '</p></div>' +
      "<span class='ReceiverChatDate'>오후" +
      (now.getHours() % 12) +
      ':' +
      now.getMinutes() +
      '</span>' +
      '</div>';
  } else if (now.getHours() === 12) {
    msgArea.innerHTML +=
      "<div class='ChatReceiverBigWrapper'>" +
      "<div class='ChatReceiverWrapper'>" +
      "<div class='ReceiverImgWrapper'>" +
      "<img class='ChatReceiverProfileImg' src='" +
      receiverPic +
      "'>" +
      '</div>' +
      "<div class='ChatMessageReceiver'>" +
      e.sender +
      '</div>' +
      "<div class='ReceiverChatMessageContent'>" +
      "<p class='ChatContent'>" +
      e.message +
      '</p></div>' +
      "<span class='ReceiverChatDate'>오후" +
      now.getHours() +
      ':' +
      now.getMinutes() +
      '</span>' + '</div>';
  } else {
    msgArea.innerHTML +=
      "<div class='ChatReceiverBigWrapper'>" +
      "<div class='ChatReceiverWrapper'>" +
      "<div class='ReceiverImgWrapper'>" +
      "<img class='ChatReceiverProfileImg' src='" +
      receiverPic +
      "'>" +
      '</div>' +
      "<div class='ChatMessageReceiver'>" +
      e.sender +
      '</div>' +
      "<div class='ReceiverChatMessageContent'>" +
      "<p class='ChatContent'>" +
      e.message +
      '</p></div>' +
      "<span class='ReceiverChatDate'>오전" +
      now.getHours() +
      ':' +
      now.getMinutes() +
      '</span>' +
      '</div>';
  }
  window.scrollTo(0, document.body.scrollHeight);
}

// 채팅 진행중 -> 메세지 보낼때 보여지는 div
function showMessageSend(e) {
  now = new Date();
  if (now.getHours() > 12) {
    msgArea.innerHTML +=
      "<div class='ChatSenderBigWrapper'>" +
      "<div class='ChatSenderWrapper'>" +
      "<div class='SenderChatMessageContent'>" +
      "<p class='ChatContent'>" +
      e.message +
      '</p></div>' +
      "<span class='SenderChatDate'>오후" +
      (now.getHours() % 12) +
      ':' +
      now.getMinutes() +
      '</span>' +
      '</div>';
  } else if (now.getHours() === 12) {
    msgArea.innerHTML +=
      "<div class='ChatSenderBigWrapper'>" +
      "<div class='SenderChatMessageContent'>" +
      "<p class='ChatContent'>" +
      e.message +
      '</p></div>' +
      "<span class='SenderChatDate'>오후" +
      now.getHours() +
      ':' +
      now.getMinutes() +
      '</span>' +
      '</div>';
  } else {
    msgArea.innerHTML +=
      "<div class='ChatSenderBigWrapper'>" +
      "<div class='ChatSenderWrapper'>" +
      "<div class='SenderChatMessageContent'>" +
      "<p class='ChatContent'>" +
      e.message +
      '</p></div>' +
      "<span class='SenderChatDate'>오전" +
      now.getHours() +
      ':' +
      now.getMinutes() +
      '</span>' +
      '</div>';
  }
  window.scrollTo(0, document.body.scrollHeight);
}

window.onload = function(e) {
document.getElementById('message').focus();
};

window.onkeydown = function(e) {
    if(e.keyCode == 27) {
		window.parent.postMessage({exit:'exit'},'*')
      disconnect();
    }
};

window.onunload = function (e) {
  disconnect();
};
