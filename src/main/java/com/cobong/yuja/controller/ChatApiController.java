package com.cobong.yuja.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.cobong.yuja.config.websocket.SocketMessage;

@Controller
public class ChatApiController {
	@MessageMapping("/chat/send")
	@SendTo("/topic/cobong")
	public SocketMessage sendMsg(@Payload SocketMessage msg) {
		return msg;
	}
	
	@MessageMapping("/chat/join")
	@SendTo("/topic/cobong")
	public SocketMessage join(@Payload SocketMessage msg, SimpMessageHeaderAccessor smha) {
		smha.getSessionAttributes().put("username", "Sender");
		return msg;
	}
}
