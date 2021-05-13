package com.cobong.yuja.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.config.websocket.SocketMessage;
import com.cobong.yuja.model.Message;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatApiController {
	private final SimpMessagingTemplate websocket;
	
	@PostMapping("/send")
	public ResponseEntity<?> sendMessage(@RequestBody SocketMessage msg){
		System.out.println(msg.getContent());
		websocket.convertAndSend("/topic/testchat", msg);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@MessageMapping("/sendMessage")
	public void msgReceived(@Payload SocketMessage msg) {
	}
	
	@SendTo("/topic/testchat")
	public SocketMessage broadcastMsg(@Payload SocketMessage msg) {
		return msg;
	}
}
