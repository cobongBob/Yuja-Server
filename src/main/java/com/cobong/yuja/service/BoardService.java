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
	public Board boardSave(BoardSaveRequestDto dto) {
		System.out.println("============> service : "+ dto);
		Board board = boardRepository.save(dto.dtoToEntity());
		return board;
	}

	@Transactional
	public Board get(Long bno) {
		Board board = boardRepository.getOne(bno);
		return board;
	}
}
