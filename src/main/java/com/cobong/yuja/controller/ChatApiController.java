package com.cobong.yuja.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.model.Message;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatApiController {
	private final SimpMessagingTemplate websocket;
	
	@MessageMapping("/ayo")
	public void msgReceived() {
		websocket.convertAndSend("/topic/testchat", "Ayo: ");
	}
}
