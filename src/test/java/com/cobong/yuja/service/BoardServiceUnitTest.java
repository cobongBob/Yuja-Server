package com.cobong.yuja.service;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.payload.request.board.BoardSaveRequestDto;
import com.cobong.yuja.payload.response.board.BoardResponseDto;
import com.cobong.yuja.repository.board.BoardRepository;
import com.cobong.yuja.service.board.BoardService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@ExtendWith(MockitoExtension.class)
public class BoardServiceUnitTest {
	@InjectMocks
	private BoardService boardService;
	
	@Mock
	private BoardRepository boardRepository;
	
	@Test
	public void save_test() {
		//given
		Board board;
		BoardSaveRequestDto dto =new BoardSaveRequestDto();
		dto.setTitle("테스트 내용1");
		dto.setContent("테스트 내용1");
		board = dto.dtoToEntity();
		
		//when
		when(boardRepository.save(board)).thenReturn(board);
		
		BoardResponseDto result = boardService.save(dto);
		
		//then
		log.info(result);
	}
	
}

