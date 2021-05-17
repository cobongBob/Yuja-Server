package com.cobong.yuja.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.service.board.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MainApiController {
	private final BoardService boardService;

	@GetMapping("/api/main/board")
	public ResponseEntity<?> getOderLiked() {
		return new ResponseEntity<>(boardService.getMainBoardData(), HttpStatus.OK);
	}
}
