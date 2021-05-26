package com.cobong.yuja.payload.request.chat;

import java.time.Instant;

import lombok.Data;

@Data
public class ChatRoomDto {
	private Long chatRoomId;
	private String writer;
	private String profilePic;
	private Instant lastMsgReceivedDate;
	private int numOfUnreadMsgs;
	
	public ChatRoomDto create(Long chatRoomId, String writer, String lastMsg) {
		ChatRoomDto room = new ChatRoomDto();
		room.setChatRoomId(chatRoomId);
		room.setWriter(writer);
		room.setProfilePic(lastMsg);
		return room;
	}
}
