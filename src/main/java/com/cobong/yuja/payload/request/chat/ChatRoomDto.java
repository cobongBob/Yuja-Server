package com.cobong.yuja.payload.request.chat;

import lombok.Data;

@Data
public class ChatRoomDto {
	private Long chatRoomId;
	private String writer;
	private String lastMsg;
	//private Date createdDate;
	
	public ChatRoomDto create(Long chatRoomId, String writer, String lastMsg) {
		ChatRoomDto room = new ChatRoomDto();
		room.setChatRoomId(chatRoomId);
		room.setWriter(writer);
		room.setLastMsg(lastMsg);
		return room;
	}
}
