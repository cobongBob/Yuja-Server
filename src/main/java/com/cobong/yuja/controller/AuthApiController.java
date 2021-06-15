package com.cobong.yuja.controller;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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

import com.cobong.yuja.config.oauth.GoogleUser;
import com.cobong.yuja.payload.request.user.LoginRequest;
import com.cobong.yuja.payload.request.user.UserSaveRequestDto;
import com.cobong.yuja.payload.request.user.YoutubeConfirmRequestDto;
import com.cobong.yuja.service.attach.ProfilePictureService;
import com.cobong.yuja.service.user.UserService;
import com.cobong.yuja.service.user.YoutubeConfirmService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth") // done
@RequiredArgsConstructor
public class AuthApiController {

	private final UserService userService;

	private final ProfilePictureService profilePictureService;
	
	private final YoutubeConfirmService youtubeConfirmService;
	
	@PostMapping("/signup")
	public ResponseEntity<?> insertUser(@Valid @RequestBody UserSaveRequestDto dto, HttpServletRequest req) {
		return new ResponseEntity<>(userService.save(dto), HttpStatus.CREATED);
	}

	@PostMapping("/verify")
	public ResponseEntity<?> verifyUser(@RequestBody Map<String, String> username) {
		return new ResponseEntity<>(userService.verify(username.get("username")), HttpStatus.OK);
	}

	@PostMapping("/checkemail")
	public ResponseEntity<?> checkid(@RequestBody Map<String, String> username) {
		if(username!= null) {
			return new ResponseEntity<>(userService.checkemail(username.get("username")), HttpStatus.OK);
		} else {
			return new ResponseEntity<>("", HttpStatus.OK);
		}
	}

	@PostMapping("/checknickname")
	public ResponseEntity<?> checknickname(@RequestBody Map<String, String> nickname) {
		return new ResponseEntity<>(userService.checkNickname(nickname.get("nickname")), HttpStatus.OK);
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse res) {
		Cookie[] cookies = userService.signIn(loginRequest);
		res.addCookie(cookies[0]);
		res.addCookie(cookies[1]);
		return new ResponseEntity<>(userService.findByUsernameForClient(loginRequest.getUsername()), HttpStatus.OK);
	}

	@GetMapping("/signout")
	public ResponseEntity<?> signOutUser(HttpServletResponse res, HttpServletRequest req) {
		Cookie[] cookies = userService.signOut();
		if(cookies != null) {
			res.addCookie(cookies[0]);
			res.addCookie(cookies[1]);
		}
		try {
			req.logout();
		} catch (ServletException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>("SignOut", HttpStatus.OK);
	}

	@PostMapping("/profile")
	public ResponseEntity<?> saveProfilePicture(@RequestParam("file") MultipartFile file) {
		return new ResponseEntity<>(profilePictureService.saveFile(file), HttpStatus.OK);
	}
	
	@PostMapping("/oauth/google")
	public ResponseEntity<?> googleOauth(@RequestBody Map<String, Object> data) {
		GoogleUser googleUser = userService.googleOauthCheck(data);
		// 201 -> 회원가입
		if(googleUser.getFlag()) {
			return new ResponseEntity<>(googleUser, HttpStatus.CREATED);
		}else {
		// 200 -> 로그인
		return new ResponseEntity<>(googleUser, HttpStatus.OK);
		}
	}
	
	
	//비밀번호 찾기 인증메일전송
	@PostMapping("/findPassword")
	public ResponseEntity<?> findPassword(@RequestBody Map<String, String> username) {
		return new ResponseEntity<>(userService.findPassword(username.get("username")), HttpStatus.OK);
	}
	
	//비밀번호 리셋 받는값은 {username :"" , password:""}
	@PostMapping("/resetPassword")
	public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> userData) {
		return new ResponseEntity<>(userService.resetPassword(userData), HttpStatus.OK);
	}
	
	@PostMapping("/youtubeconfirm")
	public ResponseEntity<?> saveYoutubeConfirm(@RequestParam("file") MultipartFile file) {
		return new ResponseEntity<>(youtubeConfirmService.saveFile(file), HttpStatus.OK);
	}
	
	/**
	 * 회원가입을 하고 그 이후에 유튜버로 전향하고 싶은 유저가 신청할때 불리는 컨트롤러
	 */
	@PostMapping("/applyyoutuber")
	public ResponseEntity<?> applyYoutuber(@RequestBody YoutubeConfirmRequestDto dto){
		return new ResponseEntity<>(youtubeConfirmService.apply(dto), HttpStatus.OK);
	}
}
