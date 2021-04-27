package com.cobong.yuja.web;

import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.cobong.yuja.controller.BoardApiController;
import com.cobong.yuja.model.Board;
import com.cobong.yuja.payload.request.BoardSaveRequestDto;
import com.cobong.yuja.payload.request.BoardUpdateRequestDto;
import com.cobong.yuja.payload.request.UserSaveRequestDto;
import com.cobong.yuja.repository.BoardRepository;
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
	@Test
	public void getOne_test() throws Exception {
		//given
		BoardSaveRequestDto dto =new BoardSaveRequestDto();
		dto.setTitle("테스트 제목1");
		dto.setContent("테스트 내용1");
		dto.setThumbnail("테스트 썸네일");
		dto.setExpiredDate(new Date());
		
		Board board = dto.dtoToEntity();
		
		//when
		Long id = 1L;
		
		this.mockMvc.perform(get("api/board/{bno}", id)
										.accept(MediaType.APPLICATION_JSON))
						.andExpect(jsonPath("$.username").value("테스트 제목1"))
						.andExpect(jsonPath("$.password").value("테스트 내용1"))
						.andExpect(jsonPath("$.nickname").value("테스트 썸네일"))
						.andExpect(jsonPath("$.realName").value("Real Name Test"))
						.andExpect(jsonPath("$.bday").value("1995-08-05"))
						.andExpect(jsonPath("$.userIp").value("165,849,949,466"))
						.andDo(MockMvcResultHandlers.print());
		
		
		
		//then
		ResultActions resultAction = mockMvc.perform(get("/api/user/get/{userId}",id).accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultAction
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.username").value("테스트 제목1"))
		.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
    public void modify_test() throws Exception {

		BoardUpdateRequestDto boardUpdateRequestDto = new BoardUpdateRequestDto();
		boardUpdateRequestDto.setTitle("수정된 제목");
		boardUpdateRequestDto.setContent("수정된 내용");
		boardUpdateRequestDto.setThumbnail("수정된 썸네일");
		
		boardService.modify(2L, boardUpdateRequestDto);

       }
	
	
}
