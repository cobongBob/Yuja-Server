package com.cobong.yuja.service;

import java.util.List;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.payload.request.BoardSaveRequestDto;
import com.cobong.yuja.payload.request.BoardUpdateRequestDto;
import com.cobong.yuja.payload.response.BoardResponseDto;

public interface BoardService {
	
	Board save(BoardSaveRequestDto dto);
	
	BoardResponseDto findById(Long bno);
	
	List<BoardResponseDto> findAll();
	
	BoardResponseDto modify(Long bno, BoardUpdateRequestDto boardUpdateRequestDto);
	
	String delete(Long bno);
	
	List<BoardResponseDto> boardsInBoardType(Long boardCode);
	
	List<BoardResponseDto> boardsUserWrote(Long userId);
	
	List<BoardResponseDto> boardsUserLiked(Long userId);
	
	List<BoardResponseDto> boardsUserCommented(Long userId);
}
