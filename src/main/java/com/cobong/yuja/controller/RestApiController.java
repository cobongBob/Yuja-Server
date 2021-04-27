package com.cobong.yuja.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.payload.request.BoardSaveRequestDto;
import com.cobong.yuja.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestApiController {
	
	private final BoardService boardService;
	
	@GetMapping("/")
	public String home() {
		return "<h1>home</h1>";
	}
	
	@PostMapping("/api/board")
	public ResponseEntity<?> insert(BoardSaveRequestDto dto) {
		System.out.println("==========>"+dto);
		return new ResponseEntity<Board>(boardService.boardSave(dto),HttpStatus.CREATED);
	}
}
