package com.cobong.yuja.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.service.notification.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NotificationApiController {
	
	private final NotificationService notificationService;
	
	// 안읽은 알림들 리스트
	@GetMapping("/api/notiUnread/{userId}") // done
	public ResponseEntity<?> getUnread(@PathVariable Long userId ) {
		return new ResponseEntity<>(notificationService.unread(userId),HttpStatus.OK);
	}
	
	// 알림 하나 읽기
	@GetMapping("/api/notiread/{notiId}") // done
	public ResponseEntity<?> getRead(@PathVariable Long notiId) {
		return new ResponseEntity<>(notificationService.read(notiId),HttpStatus.OK);
	}
	
	//
	@GetMapping("/api/findnoti/{userId}")
	public ResponseEntity<?> findAllNoti(@PathVariable Long userId) {
		return new ResponseEntity<>(notificationService.findAll(userId),HttpStatus.OK);
	}
	@DeleteMapping("/api/deletenoti/{notiId}")
	public ResponseEntity<?> deleteNoti(@PathVariable Long notiId) {
		return new ResponseEntity<>(notificationService.deletedNoti(notiId),HttpStatus.OK);
	}

}
