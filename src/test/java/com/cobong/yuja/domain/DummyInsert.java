package com.cobong.yuja.domain;

import java.util.Date;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.model.BoardComment;
import com.cobong.yuja.model.BoardLiked;
import com.cobong.yuja.model.BoardType;
import com.cobong.yuja.model.User;
import com.cobong.yuja.repository.BoardTypeRepository;
import com.cobong.yuja.repository.board.BoardRepository;
import com.cobong.yuja.repository.boardLiked.BoardLikedRepository;
import com.cobong.yuja.repository.comment.CommentRepository;
import com.cobong.yuja.repository.user.UserRepository;

@SpringBootTest
public class DummyInsert {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private BoardTypeRepository boardTypeRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private BoardLikedRepository boardLikedRepository;
	
	@Test //이놈 돌릴땐 yml에서 create로 바꿔주시고 돌린후 update로 돌려주세요
	public void insertType() {
//		1. Youtube
//		2. Editor
//		3. Thumb
		BoardType boardType = new BoardType(null,"YoutuberBoard",null);
		boardTypeRepository.save(boardType);
		boardType = new BoardType(null,"EditorBoard",null);
		boardTypeRepository.save(boardType);
		boardType = new BoardType(null,"ThumbBoard",null);
		boardTypeRepository.save(boardType);
	}
	
	
	@Test
	public void insertUser() {
		IntStream.rangeClosed(1, 102).forEach(i -> {
			User user = User.builder()
					.username("user "+i)
					.password(passwordEncoder.encode("1111"))
					.nickname("nickname "+i)
					.realName("tester "+i)
					.bday("2000-01-"+i)
					.userIp("111.111.111.111")
					.build();
			userRepository.save(user);
		});
	}
	
	@Test
	public void insertBoard() {
		IntStream.rangeClosed(1, 102).forEach(i -> {
			User user = User.builder().userId(Long.valueOf(i)).build();
			BoardType boardType = boardTypeRepository.findById(1L).orElseThrow(()-> new IllegalArgumentException("존재하지 x"));
			Board board = Board.builder()
					.boardType(boardType)
					.user(user)
					.career("신입")
					.channelName("테스트채널 "+i)
					.expiredDate(new Date())
					.manager("테스트매니저 "+i)
					.payAmount("100,000")
					.payType("건당")
					.receptionMethod("비대면")
					.recruitingNum(i)
					.tools("프리미어 프로,파이널,베가스")
					.title("테스트 제목 "+i)
					.content("테스트 내용 "+i)
					.worker("편집자")
					.hit(i)
					.build();
			boardRepository.save(board);
		});
	}
	
	@Test
	public void insertLikes() {
		IntStream.range(1, 102).forEach(i -> {
			long bno = (long) (Math.random() * 102) + 1;
			long uno = (long) (Math.random() * 102) + 1;
			Board board = Board.builder().boardId(bno).build();
			User user = User.builder().userId(uno).build();
			BoardLiked boardLiked = BoardLiked.builder()
					.board(board)
					.user(user)
					.build();
			boardLikedRepository.save(boardLiked);
		});
	}
	
	@Test
	public void insertComment() {
		IntStream.range(1, 102).forEach(i -> {
			long bno = (long) (Math.random() * 102) + 1;
			long uno = (long) (Math.random() * 102) + 1;
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
