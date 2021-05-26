package com.cobong.yuja.service.notification;

import java.util.List;

import com.cobong.yuja.payload.response.notification.NotificationResponseDto;

public interface NotificationService {
		
	List<NotificationResponseDto> unread(Long userId);
	
	NotificationResponseDto read(Long notiId);

	List<NotificationResponseDto> findAll(Long userId);

	String deletedNoti(Long notiId);

	void delete2weeksOld();
	
}
