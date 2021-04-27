package com.cobong.yuja.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.payload.request.BoardSaveRequestDto;
import com.cobong.yuja.payload.request.BoardUpdateRequestDto;
import com.cobong.yuja.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardRepository boardRepository;

	@Transactional
	public Board save(BoardSaveRequestDto dto) {
		System.out.println("============> service : " + dto);
		Board board = boardRepository.save(dto.dtoToEntity());
		return board;
	}

	@Transactional
	public Board get(Long bno) {
		Board board = boardRepository.findById(bno).orElseThrow(() -> new IllegalAccessError("해당글 없음" + bno));
		return board;
	}

	// delete
	@Transactional
	public String delete(Long bno) {
		 boardRepository.deleteById(bno);
		 return "success";
	}

	// modify
	@Transactional
	public Board modify(Long bno, BoardUpdateRequestDto boardUpdateRequestDto) {
		Board board = boardRepository.findById(bno)
				.orElseThrow(() -> new IllegalAccessError("해당글 없음" + bno));
		
		board.modify(boardUpdateRequestDto.getTitle(), boardUpdateRequestDto.getContent(), 
				boardUpdateRequestDto.getThumbnail(),boardUpdateRequestDto.getPayType(),
				boardUpdateRequestDto.getPayAmount(), boardUpdateRequestDto.getCareer(),
				boardUpdateRequestDto.getTools(), boardUpdateRequestDto.getExpiredDate());
		return board;
	}

}
