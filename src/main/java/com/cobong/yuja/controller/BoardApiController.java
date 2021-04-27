package com.cobong.yuja.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.payload.request.BoardSaveRequestDto;
import com.cobong.yuja.service.BoardService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class BoardApiController {
	private final BoardService boardService;
	
	
	@GetMapping("/")
	public String home() {
		return "<h1>home</h1>";
	}
	
	@PostMapping(path = "/api/board", consumes = "application/json")
	public ResponseEntity<?> insert(@RequestBody BoardSaveRequestDto dto) {
		System.out.println("==========>"+dto);
		return new ResponseEntity<Board>(boardService.boardSave(dto),HttpStatus.CREATED);
	}
	
	@GetMapping("/api/get/{bno}")
	public ResponseEntity<?> getBoard(@PathVariable Long bno) {
		System.out.println("==========>" + bno);
		return new ResponseEntity<Board>(boardService.get(bno),HttpStatus.OK);
	}
	
	@PostMapping("/api/test")
	public ResponseEntity<?> testOnly(@RequestBody Map<String, String> dto) {
		System.out.println("==========>"+dto);
		return new ResponseEntity<String>("Success",HttpStatus.CREATED);
	}
}
