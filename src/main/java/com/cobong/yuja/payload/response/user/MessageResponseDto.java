package com.cobong.yuja.payload.response.user;

import com.cobong.yuja.model.Message;
import com.cobong.yuja.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDto {
	private Long messageId;
	private User sender;
	private User resipeint;
	private String contents;
	
	public MessageResponseDto entityToDto(Message entity) {
		this.messageId = entity.getMessageId();
		this.sender= entity.getSender();
		this.resipeint= entity.getRecipient();
		this.contents = entity.getContents();
		return this;
	}
	
}
