package com.cobong.yuja.service.notification;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.Notification;
import com.cobong.yuja.payload.response.notification.NotificationResponseDto;
import com.cobong.yuja.repository.notification.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
	private final NotificationRepository notificationRepository; 

	@Override
	@Transactional(readOnly = true)
	public List<NotificationResponseDto> unread(Long userId) {
		List<NotificationResponseDto> notifications = new ArrayList<NotificationResponseDto>();
		List<Notification> entityList = notificationRepository.unreadNoti(userId);
		for(Notification notification : entityList) {
			NotificationResponseDto dto = new NotificationResponseDto().entityToDto(notification);
			notifications.add(dto);
		}
		return notifications;
	}
	
	@Override
	@Transactional
	public NotificationResponseDto read(Long notiId) {
		Notification notification =  notificationRepository.findById(notiId).orElseThrow(()-> new IllegalArgumentException("해당 메세지가 없습니다."));
		NotificationResponseDto dto = new NotificationResponseDto().entityToDto(notification);
		Date now = new Date();
		if(dto.getReadDate()==null) {
			notification.setReadDate(now);
		}
		return dto;
	}
	
	//모든 알림 조회
	@Override
	@Transactional(readOnly = true)
	public List<NotificationResponseDto> findAll(Long userId) {
		List<Notification> entityList = notificationRepository.findByRecipientId(userId);
		List<NotificationResponseDto> notifications = new ArrayList<NotificationResponseDto>();
		if(entityList.size()>0) {
			for(Notification notification : entityList) {
				NotificationResponseDto dto = new NotificationResponseDto().entityToDto(notification);
				notifications.add(dto);
			}
		}
		return notifications;
	}

	@Override
	@Transactional
	public String deletedNoti(Long notiId) {
		notificationRepository.deleteById(notiId);
		return "success";
	}

	@Override
	@Transactional
	public void delete2weeksOld() {
		List<Notification> notis = notificationRepository.findAll();
		LocalDateTime now = LocalDateTime.now();
		for(Notification noti: notis) {
			LocalDateTime notiTime = LocalDateTime.ofInstant(noti.getCreatedDate(), ZoneId.systemDefault());
			if(now.getDayOfYear() - notiTime.getDayOfYear() > 14) {
				notificationRepository.delete(noti);
			}
		}
	}

}
