package com.cobong.yuja.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public String insert(BoardSaveRequestDto dto) {
		System.out.println(dto);
		String result = boardService.boardSave(dto);
		return result;
	}
}
