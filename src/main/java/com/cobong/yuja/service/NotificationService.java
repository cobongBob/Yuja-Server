package com.cobong.yuja.service;

import java.util.List;

import com.cobong.yuja.payload.response.NotificationResponseDto;

public interface NotificationService {
	
//	NotificationResponseDto send(NotificationRequestDto dto);
	
	List<NotificationResponseDto> unread(Long userId);
	
	NotificationResponseDto read(Long notiId);
	
}
