package com.cobong.yuja.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.payload.request.CommentRequestDto;
import com.cobong.yuja.payload.response.CommentResponseDto;
import com.cobong.yuja.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentApiController {
	private final CommentService commentService;
	
	@GetMapping("/api/comment/{boardId}")
	public ResponseEntity<?> GetAllComment(@PathVariable Long boardId) {
		return new ResponseEntity<>(commentService.getCommentsByBoardId(boardId),HttpStatus.OK);
	}
	
	@PostMapping("/api/comment")
	public ResponseEntity<?> insertComment(@RequestBody CommentRequestDto dto) {
		dto.setDeleted(false);
		return new ResponseEntity<>(commentService.save(dto),HttpStatus.CREATED);
	}
	
	@PutMapping("/api/comment/{commentId}")
	public ResponseEntity<?> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto dto) {
		return new ResponseEntity<>(commentService.modify(commentId, dto),HttpStatus.OK);
	}
	
	@DeleteMapping("api/comment/{commentId}")
	public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
		//deleted값이 true로 바뀐다.
		return new ResponseEntity<>(commentService.deleteById(commentId),HttpStatus.OK);
	}
}
