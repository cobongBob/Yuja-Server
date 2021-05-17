package com.cobong.yuja.config.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.cobong.yuja.config.websocket.SocketMessage.MessageType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
	private final SimpMessageSendingOperations msgTemplate;
	
	@EventListener
	public void handleWebSockerConnectionListener(SessionConnectedEvent event) {
	}
	
	@EventListener
	public void handleWevSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
		if(username != null) {

            SocketMessage chatMessage = new SocketMessage();
            chatMessage.setType(MessageType.LEFT);
            chatMessage.setSender(username);

            msgTemplate.convertAndSend("/topic/public", chatMessage);
        }
	}
}
