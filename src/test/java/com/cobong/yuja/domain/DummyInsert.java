package com.cobong.yuja.domain;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.model.BoardComment;
import com.cobong.yuja.model.BoardType;
import com.cobong.yuja.model.User;
import com.cobong.yuja.repository.UserRepository;
import com.cobong.yuja.repository.board.BoardRepository;
import com.cobong.yuja.repository.comment.CommentRepository;

@SpringBootTest
public class DummyInsert {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Test
	public void insertUser() {
		IntStream.rangeClosed(1, 100).forEach(i -> {
			User user = User.builder()
					.username("user"+i)
					.password("1111")
					.nickname("nickname"+i)
					.realName("tester"+i)
					.bday("2000-01-"+i)
					.profilePic("url.jpg")
					.userIp("111.111.111.111")
					.build();
			userRepository.save(user);
		});
	}
	
	@Test
	public void insertBoard() {
		IntStream.rangeClosed(1, 100).forEach(i -> {
			User user = User.builder().userId(Long.valueOf(i)).build();
			BoardType boardType = new BoardType(1L, "FreeBoard", null);
			Board board = Board.builder()
					.boardType(boardType)
					.user(user)
					.title("테스트제목 "+i)
					.content("테스트 내용 "+i)
					.hit(0)
					.thumbnail("thumb.jpg")
					.build();
			boardRepository.save(board);
		});
	}
	
	@Test
	public void insertComment() {
		IntStream.range(1, 100).forEach(i -> {
			long bno = (long) (Math.random() * 100) + 1;
			long uno = (long) (Math.random() * 100) + 1;
			Board board = Board.builder().boardId(bno).build();
			User user = User.builder().userId(uno).build();
			BoardComment boardComment = BoardComment.builder()
					.board(board)
					.user(user)
					.content("Test Comment " + i)
					.deleted(false)
					.build();
			commentRepository.save(boardComment);
		});
	}
}
