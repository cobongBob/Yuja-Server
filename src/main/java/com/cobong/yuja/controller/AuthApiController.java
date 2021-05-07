package com.cobong.yuja.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.payload.request.user.LoginRequest;
import com.cobong.yuja.payload.request.user.UserSaveRequestDto;
import com.cobong.yuja.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth") // 설정해야댐
@RequiredArgsConstructor
public class AuthApiController {
    
    private final UserService userService;
	
	@PostMapping("/signup")
	public ResponseEntity<?> insertUser(@Valid @RequestBody UserSaveRequestDto dto) {
		return new ResponseEntity<>(userService.save(dto),HttpStatus.CREATED);
	}
    
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,HttpServletResponse res) {
		Cookie[] Cookies = userService.signIn(loginRequest);
		res.addCookie(Cookies[0]);
		res.addCookie(Cookies[1]);
		return new ResponseEntity<>(Cookies[0].getValue(), HttpStatus.OK);
	}
}
