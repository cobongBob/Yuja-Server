package com.cobong.yuja.domain;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.repository.board.BoardRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class BoardDSLTest {
	@Autowired
	private BoardRepository boardRepository;
	
	@Test
	public void dslTest1() {
		List<Board> res = boardRepository.boardsUserCommented(1L);
		//Long res = boardRepository.commentsReceived(1L);
		for(Board board: res) {
			log.info(board);
		}
		//log.info(res);
	}
}
