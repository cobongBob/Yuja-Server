package com.cobong.yuja.payload.response;

import java.util.Date;

import com.cobong.yuja.model.Notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
	private Long notiId;
	private Long userId;
	private Long commentId;
	private String type;
	private String message;
	private Date readDate;
}
