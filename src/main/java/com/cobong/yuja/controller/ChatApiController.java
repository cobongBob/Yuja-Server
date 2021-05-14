package com.cobong.yuja.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.cobong.yuja.config.websocket.SocketMessage;

@Controller
public class ChatApiController {
	@MessageMapping("/user-all") 
	@SendTo("/topic/user") 
	public SocketMessage send(@Payload SocketMessage msg){ 
		return msg; 
	} 
}
