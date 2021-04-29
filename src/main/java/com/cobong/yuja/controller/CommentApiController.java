package com.cobong.yuja.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cobong.yuja.repository.comment.CommentRepository;
import com.cobong.yuja.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentApiController {
	private final CommentRepository commentRepository;
	
	@GetMapping("")
	public void good() {
		
	}
}
