package com.cobong.yuja.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.model.BoardComment;
import com.cobong.yuja.model.BoardType;
import com.cobong.yuja.repository.CommentRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
public class CommentRepositoryUnitTest {
	@Autowired
	CommentRepository commentRepository;

	@Autowired
	private EntityManager entityManager;

	@BeforeEach
	public void init() {
		entityManager.createNativeQuery("ALTER TABLE boardComment AUTO_INCREMENT=1").executeUpdate();
	}

	@Test
	public void insertTest() {
		// given
		BoardComment comment = new BoardComment(0L, null, null, null, "테스트 댓글1", false);
		// when
		BoardComment commentEntity = commentRepository.save(comment);

		// then
		log.info(commentEntity);
		assertEquals("테스트 댓글1", commentEntity.getContent());
	}

	@Test
	public void getOneTest() {
		// given
		BoardComment comment = new BoardComment(0L, null, null, null, "테스트 댓글1", false);
		// when
		commentRepository.save(comment);
		BoardComment commentEntity = commentRepository.findById(1L).orElseThrow(()->new IllegalArgumentException("존재하지않는 댓글"));
		log.info(commentEntity);
		assertEquals("테스트 댓글1", commentEntity.getContent());
	}
}
