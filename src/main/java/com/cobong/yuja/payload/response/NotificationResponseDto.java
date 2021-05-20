package com.cobong.yuja.payload.response;

import java.util.Date;

import com.cobong.yuja.model.Notification;
import com.cobong.yuja.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
	private Long notiId;
	private User sender;
	private User resipeint;
	private String type;
	private Date readDate;
	
	public NotificationResponseDto entityToDto(Notification entity) {
		this.notiId = entity.getNotiId();
		this.resipeint = entity.getRecipient();
		this.sender = entity.getSender();
		this.type = entity.getType();
		this.readDate = entity.getReadDate();
		return this;
	}
}
