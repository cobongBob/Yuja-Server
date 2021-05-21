const chatRoomsWrapper = document.getElementsByClassName("ChatRoomsWrapper");

function deleteById(chatRoomId) {
	$.ajax({
		url: "socket/room/"+chatRoomId,
		method: "DELETE",
		success: function(data){
			const target = document.getElementById("roomNum"+chatRoomId);
			target.remove();
		}
	})
}
