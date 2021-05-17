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
	@SendTo("topic/cobong")
	public SocketMessage sendMsg(@Payload SocketMessage msg) {
		return msg;
	}
	
	@MessageMapping("/chat/join")
	@SendTo("topic/cobong")
	public SocketMessage join(@Payload SocketMessage msg, SimpMessageHeaderAccessor smha) {
		smha.getSessionAttributes().put("username", "Sender");
		return msg;
	}
	
	/***
	 * 어빌리티 참조
	 */
//	private SimpMessagingTemplate socket;
//	
//	public void send(SocketMessage msg){
//		msg.setDate(new Date());
//		socket.convertAndSend("/topic/cobong",msg); 
//	} 
//	
//	public void enter(SocketMessage msg) {
//		msg.setDate(new Date());
//		socket.convertAndSend("/topic/cobong",msg);
//	}
//	
//	public void exit(SocketMessage msg) {
//		msg.setDate(new Date());
//		socket.convertAndSend("/topic/cobong",msg);
//	}
//	
//	Set<String> connectedUsers = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
//	
//	@EventListener
//	private void onSessionConn(SessionConnectedEvent evnt) {
//		StompHeaderAccessor stmp = StompHeaderAccessor.wrap(evnt.getMessage());
//		connectedUsers.add(stmp.getSessionId());
//	}
//	
//	@EventListener
//	private void onSessionDisconn(SessionDisconnectEvent evnt) {
//		StompHeaderAccessor stmp = StompHeaderAccessor.wrap(evnt.getMessage());
//		connectedUsers.add(stmp.getSessionId());
//	}
}
