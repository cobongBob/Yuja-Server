package com.cobong.yuja.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.payload.request.user.YoutubeConfirmRequestDto;
import com.cobong.yuja.service.user.YoutubeConfirmService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/promote")
@RequiredArgsConstructor
public class YoutuberPromotionApiController {
	private final YoutubeConfirmService youtuberConfirmService;

	@GetMapping("/youtuber/{id}")
	public ResponseEntity<?> getOne(@PathVariable Long id){
		return new ResponseEntity<>(youtuberConfirmService.findById(id), HttpStatus.OK);
	}
	
	@GetMapping("/youtuber")
	public ResponseEntity<?> getAllUnauthorized(){
		return new ResponseEntity<>(youtuberConfirmService.findUnauthorized(), HttpStatus.OK);
	}
	
	@PostMapping("/youtuber")
	public ResponseEntity<?> promoteUser(@RequestBody YoutubeConfirmRequestDto dto){
		return new ResponseEntity<>(youtuberConfirmService.confirm(dto), HttpStatus.OK);
	}
	
	@DeleteMapping("/youtuber/{id}")
	public ResponseEntity<?> rejectUser(@PathVariable Long id){
		return new ResponseEntity<>(youtuberConfirmService.rejectUser(id), HttpStatus.OK);
	}
}
