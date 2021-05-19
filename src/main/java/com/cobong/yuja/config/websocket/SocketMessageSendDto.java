package com.cobong.yuja.config.websocket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocketMessageSendDto {
	private String sender;
	private String content;
	private boolean ownerOrNot;
	private String createdDate; 
	
	public SocketMessageSendDto entityToDto(SocketMessage entity) {
		SocketMessageSendDto dto = new SocketMessageSendDto();
		dto.setContent(entity.getMessage());
		dto.setSender(entity.getUser().getNickname());
		return dto;
	}
}
