package com.cobong.yuja.payload.request.user;

import com.cobong.yuja.model.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDto {
	private Long senderId;
	private Long resipeintId;
	private String contents;
	
	public Message dtoToEntity() {
		return Message
				.builder()
				.contents(this.contents)
				.build();
	}
	
}
