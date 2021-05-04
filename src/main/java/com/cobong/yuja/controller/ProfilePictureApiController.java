package com.cobong.yuja.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cobong.yuja.service.user.ProfilePictureService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProfilePictureApiController {
	private final ProfilePictureService profilePictureService;
	
	@PostMapping("/api/user/profile")
	public ResponseEntity<?> saveProfilePicture(@RequestParam("file") MultipartFile file){
		return new ResponseEntity<>(profilePictureService.saveFile(file), HttpStatus.OK);
	}
}