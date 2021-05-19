package com.cobong.yuja.config.websocket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocketMessageSendDto {
	private String sender;
	private String content;
	//private Date createdDate; 일단 기초를 구현 후 보낸 시간까지 구현해 보도록 하자.
	
	public SocketMessageSendDto entityToDto(SocketMessage entity) {
		SocketMessageSendDto dto = new SocketMessageSendDto();
		dto.setContent(entity.getMessage());
		dto.setSender(entity.getUser().getNickname());
		return dto;
	}
}
