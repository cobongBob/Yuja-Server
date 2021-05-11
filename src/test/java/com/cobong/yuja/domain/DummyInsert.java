package com.cobong.yuja.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cobong.yuja.model.Authorities;
import com.cobong.yuja.model.AuthorityNames;
import com.cobong.yuja.model.Board;
import com.cobong.yuja.model.BoardComment;
import com.cobong.yuja.model.BoardLiked;
import com.cobong.yuja.model.BoardType;
import com.cobong.yuja.model.User;
import com.cobong.yuja.repository.BoardTypeRepository;
import com.cobong.yuja.repository.board.BoardRepository;
import com.cobong.yuja.repository.boardLiked.BoardLikedRepository;
import com.cobong.yuja.repository.comment.CommentRepository;
import com.cobong.yuja.repository.user.AuthoritiesRepository;
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

	@Autowired
	private AuthoritiesRepository authRepo;
	
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
	public void insertAuth() {
		
		Authorities authorities = new Authorities(1L,AuthorityNames.GENERAL);
		authRepo.save(authorities);
		authorities = new Authorities(2L,AuthorityNames.YOUTUBER);
		authRepo.save(authorities);
		authorities = new Authorities(3L,AuthorityNames.EDITOR);
		authRepo.save(authorities);
		authorities = new Authorities(4L,AuthorityNames.THUMBNAIOR);
		authRepo.save(authorities);
		authorities = new Authorities(5L,AuthorityNames.MANAGER);
		authRepo.save(authorities);
		authorities = new Authorities(6L,AuthorityNames.ADMIN);
		authRepo.save(authorities);
	}
	
	@Test
	public void insertUser() {
		Authorities auth1 = authRepo.findById(1L).orElseThrow(()->new IllegalArgumentException("없는 권한"));
		IntStream.rangeClosed(1, 25).forEach(i -> {
			User user = User.builder()
					.username("user "+i)
					.password(passwordEncoder.encode("1111"))
					.nickname("nickname "+i)
					.realName("tester "+i)
					.bday("2000-01-"+i)
					.userIp("111.111.111.111")
					.authorities(Collections.singletonList(auth1))
					.build();
			userRepository.save(user);
		});
		Authorities auth2 = authRepo.findById(2L).orElseThrow(()->new IllegalArgumentException("없는 권한"));
		IntStream.rangeClosed(26, 50).forEach(i -> {
			User user = User.builder()
					.username("user "+i)
					.password(passwordEncoder.encode("1111"))
					.nickname("nickname "+i)
					.realName("tester "+i)
					.bday("2000-01-"+i)
					.userIp("111.111.111.111")
					.youtubeUrl("https://www.youtube.com/channel/UCVrhnbfe78ODeQglXtT1Elw")
					.authorities(Collections.singletonList(auth2))
					.build();
			userRepository.save(user);
		});
		IntStream.rangeClosed(51, 75).forEach(i -> {
			User user = User.builder()
					.username("user "+i)
					.password(passwordEncoder.encode("1111"))
					.nickname("nickname "+i)
					.realName("tester "+i)
					.bday("2000-01-"+i)
					.userIp("111.111.111.111")
					.youtubeUrl("https://www.youtube.com/channel/UCfpaSruWW3S4dibonKXENjA")
					.authorities(Collections.singletonList(auth2))
					.build();
			userRepository.save(user);
		});
		Authorities auth3 = authRepo.findById(6L).orElseThrow(()->new IllegalArgumentException("없는 권한"));
		IntStream.rangeClosed(76, 102).forEach(i -> {
			User user = User.builder()
					.username("admin "+i)
					.password(passwordEncoder.encode("1111"))
					.nickname("Admin "+i)
					.realName("Admin "+i)
					.bday("2000-01-"+i)
					.userIp("111.111.111.111")
					.authorities(Collections.singletonList(auth3))
					.build();
			userRepository.save(user);
		});
	}
	
	
	@Test
	public void insertYBoard() {
		IntStream.rangeClosed(26, 50).forEach(i -> {
			User user = User.builder().userId(Long.valueOf(i)).build();
			BoardType boardType = boardTypeRepository.findById(1L).orElseThrow(()-> new IllegalArgumentException("존재하지 x"));
			Board board = Board.builder()
					.boardType(boardType)
					.user(user)
					.career("신입")
					.channelName("데어프로그래밍 "+i)
					.expiredDate(new Date())
					.manager("테스트매니저 "+i)
					.payAmount("100,000")
					.payType("건당")
					.receptionMethod("비대면")
					.recruitingNum(i)
					.tools("프리미어 프로,파이널,베가스")
					.title("테스트 데어 "+i)
					.content("테스트 데어 "+i)
					.worker("편집자")
					.yWhen("상시모집")
					.hit(i)
					.build();
			boardRepository.save(board);
		});
		IntStream.rangeClosed(51, 75).forEach(i -> {
			User user = User.builder().userId(Long.valueOf(i)).build();
			BoardType boardType = boardTypeRepository.findById(1L).orElseThrow(()-> new IllegalArgumentException("존재하지 x"));
			Board board = Board.builder()
					.boardType(boardType)
					.user(user)
					.career("신입")
					.channelName("쯔양 "+i)
					.expiredDate(new Date())
					.manager("테스트매니저 "+i)
					.payAmount("100,000")
					.payType("건당")
					.receptionMethod("비대면")
					.recruitingNum(i)
					.tools("프리미어 프로,파이널,베가스")
					.title("테스트 쯔양 "+i)
					.content("테스트 쯔양 "+i)
					.worker("편집자")
					.yWhen("상시모집")
					.hit(i)
					.build();
			boardRepository.save(board);
		});
	}
	@Test
	public void insertEBoard() {
		IntStream.rangeClosed(1, 102).forEach(i -> {
			User user = User.builder().userId(Long.valueOf(i)).build();
			BoardType boardType = boardTypeRepository.findById(2L).orElseThrow(()-> new IllegalArgumentException("존재하지 x"));
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
					.yWhen("상시모집")
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
