package com.cobong.yuja.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public ResponseEntity<?> insertReport(@RequestBody ReportedBoardsRequestDto dto) {
		return new ResponseEntity<>(reportedBoardsService.reported(dto), HttpStatus.CREATED);
	}

	// 신고받은 모든 리스트
	@GetMapping("/api/repoarted")
	public ResponseEntity<?> getAllReport() {
		return new ResponseEntity<>(reportedBoardsService.findAll(), HttpStatus.OK);
	}

	// 신고받은 리스트 목록에서 삭제
	@DeleteMapping("/api/repoarted/{bno}")
	public ResponseEntity<?> deleteReport(@PathVariable Long bno) {
		return new ResponseEntity<>(reportedBoardsService.delete(bno), HttpStatus.OK);
		
	}
	

}
