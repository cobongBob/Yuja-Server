package com.cobong.yuja.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.cobong.yuja.model.BoardComment;
import com.cobong.yuja.model.BoardLiked;
import com.cobong.yuja.payload.request.BoardLikedRequestDto;
import com.cobong.yuja.payload.response.CommentResponseDto;
import com.cobong.yuja.repository.board.BoardRepository;
import com.cobong.yuja.repository.boardLiked.BoardLikedRepository;
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
	public String liked(BoardLikedRequestDto dto) {
		BoardLiked liked = BoardLiked.builder()
				.user(userRepository.findById(dto.getUserId()).orElseThrow(()->new IllegalArgumentException("존재하지않는 유저")))
				.board(boardRepository.findById(dto.getBoardId()).orElseThrow(()->new IllegalArgumentException("존재하지않는 글"))).build();
	
		boardLikedRepository.save(liked);
		return "Success!!";
	}

	@Override
	@Transactional
	public String disLiked(BoardLikedRequestDto dto) {
		boardLikedRepository.deleteByUserIdAndBoardId(dto.getUserId(), dto.getBoardId());
		return "Success!";
	}

}
