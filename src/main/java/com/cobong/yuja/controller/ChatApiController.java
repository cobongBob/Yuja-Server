package com.cobong.yuja.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.cobong.yuja.payload.request.chat.SocketMessageReceiveDto;
import com.cobong.yuja.service.chat.SocketMessageService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatApiController {
	private final SimpMessagingTemplate msgTemplate;
	private final SocketMessageService socketMessageService;
	
	@MessageMapping("/chat/send") //?
	public void sendMsg(SocketMessageReceiveDto msg) {
		String receiver = msg.getReceiver();
		socketMessageService.save(msg);
		msgTemplate.convertAndSend("/topic/cobong/"+receiver, msg);
	}
}
