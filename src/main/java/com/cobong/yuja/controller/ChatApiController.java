package com.cobong.yuja.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.config.websocket.SocketMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatApiController {
	private final SimpMessagingTemplate websocket;
	
	@MessageMapping("/chat/sendmessage")
	@SendTo("/topic/cobong")
	public SocketMessage sendMsg(@Payload SocketMessage msg) {
		return msg;
	}
	
	@MessageMapping("/chat/enter")
	@SendTo("/topic/cobong")
	public SocketMessage userEnter(@Payload SocketMessage msg, SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", msg.getSender());
		return msg;
	}
}
