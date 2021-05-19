package com.cobong.yuja.payload.request;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {
	private Long userId;
	private Long commentId;
	private String type;
	private String message;
	private Date readDate;
}
