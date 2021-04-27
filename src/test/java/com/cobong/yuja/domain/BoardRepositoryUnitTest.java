package com.cobong.yuja.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.model.BoardType;
import com.cobong.yuja.repository.BoardRepository;
import com.cobong.yuja.repository.BoardTypeRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
public class BoardRepositoryUnitTest {
	@Autowired
	BoardRepository boardRepository;
	
	@Autowired
	BoardTypeRepository boardTypeRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach
	public void init() {
		entityManager.createNativeQuery("ALTER TABLE board AUTO_INCREMENT=1").executeUpdate();
		entityManager.createNativeQuery("ALTER TABLE boardtype AUTO_INCREMENT=1").executeUpdate();
	}
	
	@Test
	public void insertTest() {
		//given
		BoardType boardType = new BoardType();
		boardType.setBoardName("일반게시판");
		boardTypeRepository.save(boardType);
		Board board = new Board();
		board.setTitle("테스트 제목1");
		board.setContent("테스트 내용1");
		board.setThumbnail("썸네일 URL");
		//when
		Board boardEntity = boardRepository.save(board);
		
		//then
		log.info(boardEntity);
		assertEquals("테스트 제목1", boardEntity.getTitle());
	}
}
