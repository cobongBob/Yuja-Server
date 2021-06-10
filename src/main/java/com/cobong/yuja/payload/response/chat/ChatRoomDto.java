package com.cobong.yuja.payload.response.chat;

import java.time.Instant;

import lombok.Data;

@Data
public class ChatRoomDto {
	private Long chatRoomId;
	private String writer;
	private String profilePic;
	private Instant lastMsgReceivedDate;
	private int numOfUnreadMsgs;
	private boolean hasNewChat;
	private Long notiId = 0L;
	
	public ChatRoomDto create(Long chatRoomId, String writer, String lastMsg) {
		ChatRoomDto room = new ChatRoomDto();
		room.setChatRoomId(chatRoomId);
		room.setWriter(writer);
		room.setProfilePic(lastMsg);
		return room;
	}
}
