package com.cobong.yuja.payload.request;

import java.util.Date;

import com.cobong.yuja.model.Notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {
	private Long sender;
	private Long resipeint;
	private String type;
	private Date readDate;
	
	public Notification dtoToEntity() {
		return Notification
				.builder()
				.type(this.type)
				.readDate(this.readDate)
				.build();
	}
}
