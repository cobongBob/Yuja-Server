package com.cobong.yuja.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.payload.request.board.ReportedBoardsRequestDto;
import com.cobong.yuja.service.board.ReportedBoardsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReportedApiController {
	private final ReportedBoardsService reportedBoardsService;

	
	@PostMapping("/api/reported")
	public ResponseEntity<?> insertReport(@RequestBody ReportedBoardsRequestDto dto){
		return new ResponseEntity<>(reportedBoardsService.reported(dto), HttpStatus.CREATED);
	}
	
	@GetMapping("/api/repoarted")
	public ResponseEntity<?> getallReort(){
		return new ResponseEntity<>(reportedBoardsService.findAll(),HttpStatus.OK);
	}
	// delete 만들기
}