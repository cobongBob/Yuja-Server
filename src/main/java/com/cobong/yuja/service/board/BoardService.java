package com.cobong.yuja.service.board;

import java.util.List;

import com.cobong.yuja.payload.request.board.BoardSaveRequestDto;
import com.cobong.yuja.payload.request.board.BoardUpdateRequestDto;
import com.cobong.yuja.payload.response.board.BoardResponseDto;
import com.cobong.yuja.payload.response.board.MainboardsResponseDto;

public interface BoardService {
	
	BoardResponseDto save(BoardSaveRequestDto dto);
	
	BoardResponseDto findById(Long bno, Long userId, boolean ishit);
	
	List<BoardResponseDto> findAll();
	
	BoardResponseDto modify(Long bno, BoardUpdateRequestDto boardUpdateRequestDto, Long userId);
	
	String delete(Long bno, Long userId);
	
	List<BoardResponseDto> boardsInBoardType(Long boardCode, Long userId);
	
	List<BoardResponseDto> boardsUserLiked(Long userId);
	
	List<BoardResponseDto> boardsUserCommented(Long userId);
	
	MainboardsResponseDto getMainBoardData();

	String noticePrivateSwitch(Long bno);

	List<BoardResponseDto> boardsUserWrote(Long userId, Long boardCode);
}
