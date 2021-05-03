package com.cobong.yuja.domain;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.model.BoardAttach;
import com.cobong.yuja.repository.attach.AttachRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class BoardAttachReposityTest {
	@Autowired
	AttachRepository attachRepository;
	
	@Test
	public void findByfileNameTest() {
		//List<BoardAttach> boardAttach = attachRepository.findAllByBoardId(new Board(1L, null, null, null, null, null, null, 0, null, null, null, null, null, null, null, 0, null, null));
		List<BoardAttach> boardAttach = attachRepository.findAllByBoardId(1L);
		
		log.info(boardAttach);
	}
}
