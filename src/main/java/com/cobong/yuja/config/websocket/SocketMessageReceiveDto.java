package com.cobong.yuja.config.websocket;

import lombok.Data;

@Data
public class SocketMessageReceiveDto {
	private Long chatRoomId;
	private String sender;
	private String receiver;
	private String message;
}
