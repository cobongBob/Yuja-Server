const chatRoomsWrapper = document.getElementsByClassName("ChatRoomsWrapper");

function deleteById(chatRoomId) {
	$.ajax({
		url: "socket/room/"+chatRoomId,
		method: "DELETE",
		success: function(data){
			chatRoomsWrapper.innerHTML = data;
		}
	})
}
