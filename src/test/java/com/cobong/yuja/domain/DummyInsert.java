package com.cobong.yuja.domain;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cobong.yuja.model.Authorities;
import com.cobong.yuja.model.Board;
import com.cobong.yuja.model.BoardComment;
import com.cobong.yuja.model.BoardLiked;
import com.cobong.yuja.model.BoardType;
import com.cobong.yuja.model.Thumbnail;
import com.cobong.yuja.model.User;
import com.cobong.yuja.model.enums.AuthorityNames;
import com.cobong.yuja.repository.attach.ThumbnailRepository;
import com.cobong.yuja.repository.board.BoardRepository;
import com.cobong.yuja.repository.board.BoardTypeRepository;
import com.cobong.yuja.repository.comment.CommentRepository;
import com.cobong.yuja.repository.liked.BoardLikedRepository;
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
	
	@Autowired
	private ThumbnailRepository thumbnailRepository;
	
	@Test //이놈 돌릴땐 yml에서 create로 바꿔주시고 돌린후 update로 돌려주세요
	public void insertType() {
//		1. Youtube
//		2. Editor
//		3. Thumb
//		4. Winwin
//		5. Colabo
//		6. CustomService
		BoardType boardType = new BoardType(null,"YoutuberBoard",null);
		boardTypeRepository.save(boardType);
		boardType = new BoardType(null,"EditorBoard",null);
		boardTypeRepository.save(boardType);
		boardType = new BoardType(null,"ThumbBoard",null);
		boardTypeRepository.save(boardType);
		boardType = new BoardType(null,"WinwinBoard",null);
		boardTypeRepository.save(boardType);
		boardType = new BoardType(null,"ColaboBoard",null);
		boardTypeRepository.save(boardType);
		boardType = new BoardType(null,"CustomServiceBoard",null);
		boardTypeRepository.save(boardType);
		boardType = new BoardType(null,"FreeBoard",null);
		boardTypeRepository.save(boardType);
		boardType = new BoardType(null,"ReportBoard",null);
		boardTypeRepository.save(boardType);
		boardType = new BoardType(null,"NoticeBoard",null);
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
		authorities = new Authorities(4L,AuthorityNames.THUMBNAILER);
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
					.nickname("nickname"+i)
					.realName("tester"+i)
					.bday("2000-01-"+i)
					.authorities(Collections.singletonList(auth1))
					.build();
			userRepository.save(user);
		});
		Authorities auth2 = authRepo.findById(2L).orElseThrow(()->new IllegalArgumentException("없는 권한"));
		IntStream.rangeClosed(26, 50).forEach(i -> {
			User user = User.builder()
					.username("user "+i)
					.password(passwordEncoder.encode("1111"))
					.nickname("데어"+i)
					.realName("최주호"+i)
					.bday("2000-01-"+i)
					.youtubeUrl("https://www.youtube.com/channel/UCVrhnbfe78ODeQglXtT1Elw")
					.authorities(Collections.singletonList(auth2))
					.build();
			userRepository.save(user);
		});
		IntStream.rangeClosed(51, 75).forEach(i -> {
			User user = User.builder()
					.username("user "+i)
					.password(passwordEncoder.encode("1111"))
					.nickname("쯔양"+i)
					.realName("박정원"+i)
					.bday("2000-01-"+i)
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
					.nickname("Admin"+i)
					.realName("어드민"+i)
					.bday("2000-01-"+i)
					.authorities(Collections.singletonList(auth3))
					.build();
			userRepository.save(user);
		});
	}

	@Test // 유튜버 게시글 더미
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
					.title("테스트 데어테스트 데어테스트 데어테스트 데어테스트 데어테스트 데어 "+i)
					.content("테스트 데어테스트 데어테스트 데어테스트 데어테스트 데어테스트 데어테스트 데어테스트 데어테스트 데어테스트 데어테스트 데어테스트 데어테스트 데어테스트 데어테스트 데어테스트 데어테스트 데어테스트 데어 "+i)
					.worker("편집자")
					.yWhen("상시모집")
					.hit(i)
					.boardUpdatedDate(Instant.now())
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
					.payAmount("2,100,000")
					.payType("연봉")
					.receptionMethod("비대면")
					.recruitingNum(i)
					.tools("프리미어 프로,파이널,베가스")
					.title("테스트 쯔양테스트 쯔양테스트 쯔양테스트 쯔양테스트 쯔양테스트 쯔양테스트 쯔양테스트 테스트 쯔양 "+i)
					.content("테스트 쯔양테스트 쯔양테스트 쯔양테스트 쯔양테스트 쯔양테스트 쯔양테스트 쯔양테스트 쯔양테스트 쯔양테스트 쯔양테스트 쯔양테스트 쯔양테스트 쯔양테스트 쯔양테스트 쯔양테스트 쯔양테스트 쯔양테스트 쯔양 "+i)
					.worker("편집자")
					.yWhen("상시모집")
					.hit(i)
					.boardUpdatedDate(Instant.now())
					.build();
			boardRepository.save(board);
		});
	}
	@Test // winwin게시판 더미
	public void insertWBoard() {
		IntStream.rangeClosed(1, 102).forEach(i -> {
			User user = User.builder().userId(Long.valueOf(i)).build();
			BoardType boardType = boardTypeRepository.findById(4L).orElseThrow(()-> new IllegalArgumentException("존재하지 x"));
			Board board = Board.builder()
					.boardType(boardType)
					.user(user)
					.title("테스트 윈 제목테스트 윈 제목테스트 윈 제목테스트 윈 제목테스트 윈 제목테스트 윈 제목테스트 윈 제목 "+i)
					.content("테스트 윈 내용테스트 윈 내용테스트 윈 내용테스트 윈 내용테스트 윈 내용테스트 윈 내용테스트 윈 내용테스트 윈 내용테스트 윈 내용테스트 윈 내용테스트 윈 내용테스트 윈 내용테스트 윈 내용테스트 윈 내용테스트 윈 내용 "+i)
					.hit(i)
					.boardUpdatedDate(Instant.now())
					.build();
			boardRepository.save(board);
		});
	}
	@Test //콜라보게시판 더미
	public void insertCBoard() {
		IntStream.rangeClosed(1, 102).forEach(i -> {
			User user = User.builder().userId(Long.valueOf(i)).build();
			BoardType boardType = boardTypeRepository.findById(5L).orElseThrow(()-> new IllegalArgumentException("존재하지 x"));
			Board board = Board.builder()
					.boardType(boardType)
					.user(user)
					.title("테스트 콜라보 제목테스트 콜라보 제목테스트 콜라보 제목테스트 콜라보 제목테스트 콜라보 제목테스트 콜라보 제목 "+i)
					.content("테스트 콜라보 내용테스트 콜라보 내용테스트 콜라보 내용테스트 콜라보 내용테스트 콜라보 내용테스트 콜라보 내용테스트 콜라보 내용테스트 콜라보 내용테스트 콜라보 내용테스트 콜라보 내용테스트 콜라보 내용테스트 콜라보 내용테스트 콜라보 내용 "+i)
					.hit(i)
					.boardUpdatedDate(Instant.now())
					.build();
			boardRepository.save(board);
		});
	}
	@Test //편집자 게시글 번호
	public void insertEBoard() {
		IntStream.rangeClosed(1, 102).forEach(i -> {
			User user = User.builder().userId(Long.valueOf(i)).build();
			BoardType boardType = boardTypeRepository.findById(2L).orElseThrow(()-> new IllegalArgumentException("존재하지 x"));
			Board board = Board.builder()
					.boardType(boardType)
					.user(user)
					.title("테스트 에디터 제목테스트 에디터 제목테스트 에디터 제목테스트 에디터 제목테스트 에디터 제목테스트 에디터 제목 "+i)
					.content("테스트 에디터 내용테스트 에디터 내용테스트 에디터 내용테스트 에디터 내용테스트 에디터 내용테스트 에디터 내용테스트 에디터 내용테스트 에디터 내용테스트 에디터 내용테스트 에디터 내용테스트 에디터 내용테스트 에디터 내용테스트 에디터 내용 "+i)
					.tools("프리미어 프로,파이널,베가스")
					.previewImage("https://img.youtube.com/vi/JSyw7OEbMqM/hqdefault.jpg")
					.payAmount("100,000")
					.payType("건당")
					.hit(i)
					.boardUpdatedDate(Instant.now())
					.build();
			boardRepository.save(board);
		});
	}
	@Test //썸네일러 게시글 번호
	public void insertTBoard() {
		IntStream.rangeClosed(1, 102).forEach(i -> {
			User user = User.builder().userId(Long.valueOf(i)).build();
			BoardType boardType = boardTypeRepository.findById(3L).orElseThrow(()-> new IllegalArgumentException("존재하지 x"));
			Board board = Board.builder()
					.boardType(boardType)
					.user(user)
					.title("테스트 썸네일 제목테스트 썸네일 제목테스트 썸네일 제목테스트 썸네일 제목테스트 썸네일 제목테스트 썸네일 제목 "+i)
					.content("테스트 썸네일 내용테스트 썸네일 내용테스트 썸네일 내용테스트 썸네일 내용테스트 썸네일 내용테스트 썸네일 내용테스트 썸네일 내용테스트 썸네일 내용테스트 썸네일 내용테스트 썸네일 내용테스트 썸네일 내용테스트 썸네일 내용테스트 썸네일 내용테스트 썸네일 내용 "+i)
					.tools("포토샵")
					.payAmount("50,000")
					.payType("건당")
					.hit(i)
					.boardUpdatedDate(Instant.now())
					.build();
			boardRepository.save(board);
		});
	}
	@Test //건의 게시글 번호
	public void insertCusBoard() {
		IntStream.rangeClosed(1, 102).forEach(i -> {
			User user = User.builder().userId(Long.valueOf(i)).build();
			BoardType boardType = boardTypeRepository.findById(6L).orElseThrow(()-> new IllegalArgumentException("존재하지 x"));
			Board board = Board.builder()
					.boardType(boardType)
					.user(user)
					.title("테스트 제목 "+i)
					.content("테스트 내용 "+i)
					.hit(i)
					.boardUpdatedDate(Instant.now())
					.build();
			boardRepository.save(board);
		});
	}
	@Test //자유 게시글 번호
	public void insertFreeBoard() {
		IntStream.rangeClosed(1, 102).forEach(i -> {
			User user = User.builder().userId(Long.valueOf(i)).build();
			BoardType boardType = boardTypeRepository.findById(7L).orElseThrow(()-> new IllegalArgumentException("존재하지 x"));
			Board board = Board.builder()
					.boardType(boardType)
					.user(user)
					.title("테스트 제목 "+i)
					.content("테스트 내용 "+i)
					.hit(i)
					.boardUpdatedDate(Instant.now())
					.build();
			boardRepository.save(board);
		});
	}
	
	@Test
	public void insertLikes() {
		IntStream.range(1, 658).forEach(i -> {
			long bno = (long) (Math.random() * 650) + 1;
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
		IntStream.range(1, 458).forEach(i -> {
			long bno = (long) (Math.random() * 650) + 1;
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
	
	@Test // files/thumbnail폴더에 yuzu05.png파일 넣어주셔야합니다.
	public void insertThumb() {
		IntStream.range(357, 458).forEach(i -> {
			Board board = Board.builder().boardId(Long.valueOf(i)).build();
			Thumbnail thumb = Thumbnail.builder()
					.fileName("yuzu05.png")
					.board(board)
					.uploadPath("임시입니다")
					.tempPath("임시입니다")
					.origFilename("임시")
					.flag(true)
					.originalFileTemp("yuzu05.png")
					.originalFileDest("yuzu05.png")
					.build();
			thumbnailRepository.save(thumb);
		});
	}
}
