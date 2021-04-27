package com.cobong.yuja.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.cobong.yuja.controller.BoardApiController;
import com.cobong.yuja.model.Board;
import com.cobong.yuja.payload.request.BoardSaveRequestDto;
import com.cobong.yuja.service.BoardService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@WebMvcTest(BoardApiController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class BoardControllerUnitTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private BoardService boardService;
	
	@Test
	public void save_test() throws Exception {
		//given
		BoardSaveRequestDto dto =new BoardSaveRequestDto();
		dto.setTitle("테스트 제목1");
		dto.setContent("테스트 내용1");
		dto.setThumbnail("테스트 썸네일");
		//dto.setAttache(new ArrayList<>());
		
		dto.setExpiredDate(new Date());
		Board board = dto.dtoToEntity();
		
		Board resultBoard = new Board();
		resultBoard.setBoardId(1L);
		resultBoard.setTitle("테스트 제목1");
		resultBoard.setContent("테스트 내용1");
		resultBoard.setThumbnail("테스트 썸네용");
		String content = new ObjectMapper().writeValueAsString(board);
//		given(boardService.boardSave(dto)).willReturn(resultBoard);
		//when
		when(boardService.save(dto)).thenReturn(resultBoard);
		
		this.mockMvc.perform(post("/api/board")
										.contentType(MediaType.APPLICATION_JSON)
										.content(content)
										.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.title").value("테스트 제목1"))
				.andExpect(jsonPath("$.content").value("테스트 내용1"))
				.andExpect(jsonPath("$.thumbnail").value("테스트 썸네용"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	
}
