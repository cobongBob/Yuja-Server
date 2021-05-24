package com.cobong.yuja.service;

import java.util.List;

import com.cobong.yuja.payload.response.NotificationResponseDto;

public interface NotificationService {
	
//	NotificationResponseDto send(NotificationRequestDto dto);
	
	List<NotificationResponseDto> unread(Long userId);
	
	NotificationResponseDto read(Long notiId);

	List<NotificationResponseDto> findAll(Long userId);

	String deletedNoti(Long notiId);

	void delete2weeksOld();
	
}
