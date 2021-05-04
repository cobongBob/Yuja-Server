package com.cobong.yuja.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.payload.request.user.UserSaveRequestDto;
import com.cobong.yuja.payload.request.user.UserUpdateRequestDto;
import com.cobong.yuja.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserApiController {

	private final UserService userService;
	
	@PostMapping(path = "/api/user")
	public ResponseEntity<?> insertUser(@RequestBody UserSaveRequestDto dto) {
		System.out.println("***********************"+dto);
		return new ResponseEntity<>(userService.save(dto),HttpStatus.CREATED);
	}
	
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
		return new ResponseEntity<>(userService.modify(bno,userUpdateRequestDto),HttpStatus.OK);
	}
	
	@DeleteMapping("/api/user/{bno}")
	public ResponseEntity<?> deleteUser(@PathVariable Long bno){
		return new ResponseEntity<>(userService.delete(bno),HttpStatus.OK);
	}
}
