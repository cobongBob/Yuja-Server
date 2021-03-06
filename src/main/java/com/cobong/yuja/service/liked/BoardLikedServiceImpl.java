package com.cobong.yuja.service.liked;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.cobong.yuja.model.BoardLiked;
import com.cobong.yuja.repository.board.BoardRepository;
import com.cobong.yuja.repository.liked.BoardLikedRepository;
import com.cobong.yuja.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardLikedServiceImpl implements BoardLikedService{
	private final BoardLikedRepository boardLikedRepository;
	private final BoardRepository boardRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public String liked(Long bno, Long userId) {
		// 좋아요 한번만 할수있게끔 (조건문없으면 중복가능해서 카운트가 1이상 증가함)
		if(boardLikedRepository.duplicateCheck(userId, bno).size() == 0 ) {
		BoardLiked liked = BoardLiked.builder()
				.user(userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("존재하지않는 유저")))
				.board(boardRepository.findById(bno).orElseThrow(()->new IllegalArgumentException("존재하지않는 글"))).build();
		boardLikedRepository.save(liked);
		}
		return "Success!!";
	}

	@Override
	@Transactional
	public String disLiked(Long bno, Long userId) {
		boardLikedRepository.deleteByUserIdAndBoardId(userId, bno);
		return "Success!";
	}
}
