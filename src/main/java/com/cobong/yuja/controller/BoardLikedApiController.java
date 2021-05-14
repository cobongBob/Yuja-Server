package com.cobong.yuja.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.config.auth.PrincipalDetails;
import com.cobong.yuja.service.board.BoardLikedService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardLikedApiController {
	private final BoardLikedService boardLikedService;
	
	@PostMapping("/api/board/liked/{bno}")
	public ResponseEntity<?> insertLike(@PathVariable Long bno){
		PrincipalDetails principalDetails = null;
    	Long userId = 0L;
    	if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PrincipalDetails) {
    		principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userId = principalDetails.getUserId();
		}
		return new ResponseEntity<>(boardLikedService.liked(bno,userId), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/api/board/liked")
	public ResponseEntity<?> deleteLike(@PathVariable Long bno){
		PrincipalDetails principalDetails = null;
    	Long userId = 0L;
    	if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof PrincipalDetails) {
    		principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userId = principalDetails.getUserId();
		}
		return new ResponseEntity<>(boardLikedService.disLiked(bno, userId), HttpStatus.OK);
	}
}
