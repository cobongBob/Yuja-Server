package com.cobong.yuja.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.model.User;
import com.cobong.yuja.payload.request.UserSaveRequestDto;
import com.cobong.yuja.payload.request.UserUpdateRequestDto;
import com.cobong.yuja.payload.response.UserResponseDto;
import com.cobong.yuja.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserApiController {

	private final UserService userService;
	
	@PostMapping(path = "/api/user")
	public ResponseEntity<?> insertUser(@RequestBody UserSaveRequestDto dto) {
		System.out.println("==========>"+dto);
		return new ResponseEntity<User>(userService.save(dto),HttpStatus.CREATED);
	}
	
	@GetMapping("/api/user/{userId}")
	public ResponseEntity<?> getOneUser(@PathVariable Long userId) {
		System.out.println("==========>" + userId);
		return new ResponseEntity<UserResponseDto>(userService.findById(userId),HttpStatus.OK);
	}
	
	@GetMapping("/api/user")
	public ResponseEntity<?> getAllUser(){
		return new ResponseEntity<List<UserResponseDto>>(userService.findAll(),HttpStatus.OK);
	}
	
	// update
	@PutMapping("/api/user/{bno}")
	public ResponseEntity<?> modifyUser(@PathVariable Long bno, @RequestBody UserUpdateRequestDto userUpdateRequestDto){
		System.out.println("=============>"+ bno+ "   "+userUpdateRequestDto);
		return new ResponseEntity<UserResponseDto>(userService.modify(bno,userUpdateRequestDto),HttpStatus.OK);
	}
	
	@DeleteMapping("/api/user/{bno}")
	public ResponseEntity<?> deleteUser(@PathVariable Long bno){
		return new ResponseEntity<String>(userService.delete(bno),HttpStatus.OK);
	}
}
