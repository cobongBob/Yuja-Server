package com.cobong.yuja.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.payload.request.BoardSaveRequestDto;
import com.cobong.yuja.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardRepository boardRepository;
	
	@Transactional
	public String boardSave(BoardSaveRequestDto dto) {
		Board board = boardRepository.save(dto.dtoToEntity());
		
		return board != null?"성공":"실패";
	}
}
