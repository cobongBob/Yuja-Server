package com.cobong.yuja.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocketMessage {
	private String sender;
	private String content;
	private MessageType type;
	
	public enum MessageType{
		CHAT,
		JOIN,
		LEFT
	}
}
