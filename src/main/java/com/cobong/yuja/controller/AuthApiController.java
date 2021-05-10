package com.cobong.yuja.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cobong.yuja.payload.request.user.LoginRequest;
import com.cobong.yuja.payload.request.user.UserSaveRequestDto;
import com.cobong.yuja.service.user.ProfilePictureService;
import com.cobong.yuja.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth") // 설정해야댐
@RequiredArgsConstructor
public class AuthApiController {
    
    private final UserService userService;
    
    private final ProfilePictureService profilePictureService;
	
	@PostMapping("/signup")
	public ResponseEntity<?> insertUser(@Valid @RequestBody UserSaveRequestDto dto) {
		return new ResponseEntity<>(userService.save(dto),HttpStatus.CREATED);
	}
	
	@PostMapping("/verify")
	public ResponseEntity<?> verifyUser(@RequestBody Map<String, String> username) {
		return new ResponseEntity<>(userService.verify(username.get("username")), HttpStatus.OK);
	}
	
	@PostMapping("/checkid")
	public ResponseEntity<?> checkid(@RequestBody String username){
		return new ResponseEntity<>(userService.checkId(username), HttpStatus.OK);
	}
	
	@PostMapping("/checknickname")
	public ResponseEntity<?> checknickname(@RequestBody String nickname){
		return new ResponseEntity<>(userService.checkNickname(nickname), HttpStatus.OK);
	}
    
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,HttpServletResponse res) {
		Cookie[] cookies = userService.signIn(loginRequest);
		res.addCookie(cookies[0]);
		res.addCookie(cookies[1]);
		return new ResponseEntity<>(cookies[0].getValue(), HttpStatus.OK);
	}
	
	@PostMapping("/signout")
	public ResponseEntity<?> signOutUser(HttpServletResponse res) {
		Cookie[] cookies = userService.signOut();
		res.addCookie(cookies[0]);
		res.addCookie(cookies[1]);
		return new ResponseEntity<>("SignOut", HttpStatus.OK);
	}
	@PostMapping("/profile")
	public ResponseEntity<?> saveProfilePicture(@RequestParam("file") MultipartFile file){
		return new ResponseEntity<>(profilePictureService.saveFile(file), HttpStatus.OK);
	}
}
