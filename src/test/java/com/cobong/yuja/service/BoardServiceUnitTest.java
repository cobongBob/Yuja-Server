package com.cobong.yuja.service;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.payload.request.BoardSaveRequestDto;
import com.cobong.yuja.repository.BoardRepository;

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
		dto.setThumbnail("테스트 썸네일");
		board = dto.dtoToEntity();
		
		//when
		when(boardRepository.save(board)).thenReturn(board);
		
		Board result = boardService.boardSave(dto);
		
		//then
		log.info(result);
		assertEquals(result, "성공");
	}
	
}
