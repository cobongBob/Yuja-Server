package com.cobong.yuja.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.config.auth.PrincipalDetails;
import com.cobong.yuja.payload.request.user.UserUpdateRequestDto;
import com.cobong.yuja.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserApiController {

	private final UserService userService;
	
	@GetMapping("/api/user/{userId}")
	public ResponseEntity<?> getOneUser(@PathVariable Long userId) {
		return new ResponseEntity<>(userService.findById(userId),HttpStatus.OK);
	}
	
	@GetMapping("/api/user")
	public ResponseEntity<?> getAllUser(){
		return new ResponseEntity<>(userService.findAll(),HttpStatus.OK);
	}
	
	@PutMapping("/api/user/{bno}")
	public ResponseEntity<?> modifyUser(@PathVariable Long bno, @RequestBody UserUpdateRequestDto userUpdateRequestDto){
		PrincipalDetails principalDetails = null;
    	Long userId = 0L;
    	if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PrincipalDetails) {
    		principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userId = principalDetails.getUserId();
		}
		return new ResponseEntity<>(userService.modify(bno,userUpdateRequestDto, userId),HttpStatus.OK);
	}
	
	@DeleteMapping("/api/user/{bno}")
	public ResponseEntity<?> deleteUser(@PathVariable Long bno){
		PrincipalDetails principalDetails = null;
    	Long userId = 0L;
    	if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PrincipalDetails) {
    		principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userId = principalDetails.getUserId();
		}
		return new ResponseEntity<>(userService.delete(bno, userId),HttpStatus.OK);
	}
}
