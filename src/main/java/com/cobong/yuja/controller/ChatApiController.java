package com.cobong.yuja.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.cobong.yuja.model.SocketMessage;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatApiController {
	private final SimpMessagingTemplate msgTemplate;
	
//	@MessageMapping("/chat/createRoom")
//	public void createRoom(@Payload SocketMessage msg) {
//		msgTemplate.convertAndSendToUser(msg.getRecipient(), "/topic/cobong", msg.getContent());
//	}
	
	@MessageMapping("/chat/send")
	@SendTo("/topic/cobong")
	public void sendMsg(@Payload SocketMessage msg) {
		msgTemplate.convertAndSend("/topic/cobong", msg);
	}
	
	@MessageMapping("/chat/join")
	@SendTo("/topic/cobong")
	public SocketMessage join(@Payload SocketMessage msg, SimpMessageHeaderAccessor smha) {
		smha.getSessionAttributes().put("username", "Sender");
		return msg;
	}


}
