package com.cobong.yuja.domain;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cobong.yuja.model.BoardComment;
import com.cobong.yuja.payload.response.CommentResponseDto;
import com.cobong.yuja.repository.comment.CommentRepository;
import com.cobong.yuja.service.CommentService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class CommentDSLTest {
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private CommentService commentService;
	
	@Test
	public void commentListTestWithRepo() {
		List<BoardComment> list = commentRepository.findCommentByBoardId(14L);
		for(BoardComment comment:list) {
			log.info(comment);
		}
	}
	
	@Test
	public void commentListTestWithService() {
		List<CommentResponseDto> list = commentService.getCommentsByBoardId(14L);
		for(CommentResponseDto comment:list) {
			log.info(comment);
		}
	}
	
	
}
