package com.cobong.yuja.config.websocket;

import java.util.HashSet;
import java.util.Set;

import org.springframework.web.socket.WebSocketSession;

import lombok.Data;

@Data
public class ChatRoomDto {
	private int chatRoomId;
	private Set<WebSocketSession> sessions = new HashSet<WebSocketSession>();
	
	public static ChatRoomDto create() {
		ChatRoomDto room = new ChatRoomDto();
		room.setChatRoomId(1);
		
		return room;
	}
}
