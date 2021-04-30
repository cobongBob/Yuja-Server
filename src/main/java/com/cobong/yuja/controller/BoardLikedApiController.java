package com.cobong.yuja.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.payload.request.board.BoardLikedRequestDto;
import com.cobong.yuja.service.board.BoardLikedService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardLikedApiController {
	private final BoardLikedService boardLikedService;
	
	@PostMapping("/api/board/liked")
	public ResponseEntity<?> insertLike(@RequestBody BoardLikedRequestDto dto){
		return new ResponseEntity<>(boardLikedService.liked(dto), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/api/board/liked")
	public ResponseEntity<?> deleteLike(@RequestBody BoardLikedRequestDto dto){
		return new ResponseEntity<>(boardLikedService.disLiked(dto), HttpStatus.OK);
	}
}
