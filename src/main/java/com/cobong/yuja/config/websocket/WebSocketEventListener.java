package com.cobong.yuja.config.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
	private final SimpMessageSendingOperations msgTemplate;
	
	@EventListener
	public void handleWebSockerConnectionListener(SessionConnectedEvent event) {
		System.out.println("a user connected!!!!!!");
	}
	
	@EventListener
	public void handleWevSocketDisconnectListener(SessionDisconnectEvent event) {
	
		String whoLeft = "Some asshole left the show";
		msgTemplate.convertAndSend("/topic/cobong", whoLeft);
	}
}
