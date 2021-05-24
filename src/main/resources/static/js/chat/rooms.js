const chatRoomsWrapper = document.getElementsByClassName("ChatRoomsWrapper");

function deleteById(chatRoomId) {
	$.ajax({
		url: "socket/room/"+chatRoomId,
		method: "DELETE",
		success: function(data){
			const target = document.getElementById("roomNum"+chatRoomId);
			target.remove();
		},
		error: function(data){
			$(".ChatListTitle").append("<h3 style='color: red'>"+e.responseJSON.message+"<h3>");
		}
	})
}

function enterRoom(chatRoomId){
	$.ajax({
		url: "/socket/chat/"+chatRoomId,
		success: function(data){
			location.href = "/socket/chat/"+chatRoomId;
		},
		error: function(e){
			$(".ChatListTitle").append("<h3 style='color: red'>"+e.responseJSON.message+"<h3>");
		}
	})
}

function findRoom(){
	if($("#receiver").val().includes("<") || $("#receiver").val().includes(">")){
		$("#ErrorMsg").html("<h3 style='color: red'>특수문자는 검색할수 없습니다<h3>");
		return false;
	}
	$.ajax({
		url: "/socket/room?receiver="+$("#receiver").val(),
		method: "POST",
		success: function(data){
			location.href = data;
		},
		error: function(e){
			$("#ErrorMsg").html("<h3 style='color: red'>"+e.responseJSON.message+"<h3>");
		}
	})
	return false;
}