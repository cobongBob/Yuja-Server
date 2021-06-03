package com.cobong.yuja.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
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
	
	@GetMapping("/api/user/{userId}") // done
	public ResponseEntity<?> getOneUser(@PathVariable Long userId) {
		PrincipalDetails principalDetails = null;
    	Long aUserId = 0L;
    	if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PrincipalDetails) {
    		principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			aUserId = principalDetails.getUserId();
		}
		return new ResponseEntity<>(userService.findById(userId, aUserId),HttpStatus.OK);
	}
	
	@GetMapping("/api/user") // test 해바야댐
	public ResponseEntity<?> getAllUser(){
		PrincipalDetails principalDetails = null;
    	String authorities = null;
    	if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PrincipalDetails) {
    		principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		authorities = principalDetails.getAuthorities().stream()
	                .map(GrantedAuthority::getAuthority)
	                .collect(Collectors.joining(","));
		}
    	if(authorities.contains("ADMIN") || authorities.contains("MANAGER")) {
    		return new ResponseEntity<>(userService.findAll(),HttpStatus.OK);
    	} else {
    		return new ResponseEntity<>("권한이 없습니다.",HttpStatus.OK);
    	}
	}
	
	@PutMapping("/api/user/{bno}") // done
	public ResponseEntity<?> modifyUser(@PathVariable Long bno, @RequestBody UserUpdateRequestDto userUpdateRequestDto){
		PrincipalDetails principalDetails = null;
    	Long userId = 0L;
    	if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PrincipalDetails) {
    		principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userId = principalDetails.getUserId();
		}
		return new ResponseEntity<>(userService.modify(bno,userUpdateRequestDto, userId),HttpStatus.OK);
	}
	
	@DeleteMapping("/api/user/{bno}") // done
	public ResponseEntity<?> deleteUser(@PathVariable Long bno){
		PrincipalDetails principalDetails = null;
    	Long userId = 0L;
    	if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PrincipalDetails) {
    		principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userId = principalDetails.getUserId();
		}
		return new ResponseEntity<>(userService.delete(bno, userId),HttpStatus.OK);
	}
	@DeleteMapping("/api/user/remove/{uno}") // done
	public ResponseEntity<?> removeUser(@PathVariable Long uno){
		PrincipalDetails principalDetails = null;
		Long userId = 0L;
		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PrincipalDetails) {
			principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userId = principalDetails.getUserId();
		}
		return new ResponseEntity<>(userService.remove(uno, userId),HttpStatus.OK);
	}
	
	@PutMapping("/api/banned/{uno}") // done
	public ResponseEntity<?> setBanned(@PathVariable Long uno){
		PrincipalDetails principalDetails = null;
    	String authorities = null;
    	if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PrincipalDetails) {
    		principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		authorities = principalDetails.getAuthorities().stream()
	                .map(GrantedAuthority::getAuthority)
	                .collect(Collectors.joining(","));
		}
    	if(authorities.contains("ADMIN")) {
    		return new ResponseEntity<>(userService.banned(uno),HttpStatus.OK);
    	} else {
    		return new ResponseEntity<>("권한이 없습니다.",HttpStatus.OK);
    	}
	}
}
