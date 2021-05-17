package com.cobong.yuja.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.payload.request.user.UserUpdateRequestDto;
import com.cobong.yuja.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BannedApiController {
	
	private final UserService userService;
	
	@PutMapping("/api/banned/{bno}")
	public ResponseEntity<?> setBanned(@PathVariable Long bno, @RequestBody UserUpdateRequestDto userUpdateRequestDto){
		return new ResponseEntity<>(userService.banned(bno,userUpdateRequestDto),HttpStatus.OK);
	}

}
