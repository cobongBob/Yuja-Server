package com.cobong.yuja.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.payload.request.Board.BoardSaveRequestDto;
import com.cobong.yuja.repository.board.BoardRepository;
import com.cobong.yuja.service.board.BoardService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Transactional
@AutoConfigureMockMvc // 단위테스트용 @WebMvcTest 에는 포함되어있다.
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) 
public class BoardControllerIntegreTest {
	
	@Autowired
	private MockMvc mockMvc; //@@AutoConfigureMockMvc가 필요하다.
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach
	public void init() {
		entityManager.createNativeQuery("ALTER TABLE board AUTO_INCREMENT=1").executeUpdate();
		for(int i = 0; i<3; i++) {
			BoardSaveRequestDto boardSaveRequestDto = new BoardSaveRequestDto();
			boardSaveRequestDto.setTitle("테스트 제목"+i);
			boardSaveRequestDto.setContent("테스트 내용"+i);
			boardSaveRequestDto.setThumbnail("테스트 썸네일"+i);
			boardSaveRequestDto.setExpiredDate(new Date());
			boardService.save(boardSaveRequestDto);
		}
	}
	
	@Test
	public void save_test() throws Exception {
		//given
		BoardSaveRequestDto dto =new BoardSaveRequestDto();
		dto.setTitle("테스트 제목4");
		dto.setContent("테스트 내용4");
		dto.setThumbnail("테스트 썸네일4");
		Board board = dto.dtoToEntity();
		String content = new ObjectMapper().writeValueAsString(board);
		
		ResultActions resultActions = mockMvc.perform(post("/api/board")
										.contentType(MediaType.APPLICATION_JSON_UTF8)
										.content(content)
										.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultActions
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.title").value("테스트 제목4"))
				.andExpect(jsonPath("$.content").value("테스트 내용4"))
				.andExpect(jsonPath("$.thumbnail").value("테스트 썸네일4"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void delete_test() throws Exception {
		Long id = 1L;
		ResultActions resultAction = this.mockMvc.perform(delete("/{bno}", id)
				.accept(MediaType.TEXT_PLAIN));
		
		MvcResult requestResult = resultAction.andReturn();
	    String result = requestResult.getResponse().getContentAsString();
		assertEquals("success", result);
	}
	
	@Test
	public void getOne_test() throws Exception {
	Long id = 1L;
	this.mockMvc.perform(get("/{bno}", id)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.title").value("테스트 제목0"))
					.andExpect(jsonPath("$.content").value("테스트 내용0"))
					.andExpect(jsonPath("$.thumbnail").value("테스트 썸네일0"))
					.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void getAll_test() throws Exception {
		this.mockMvc.perform(get("/api/board")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print());
	}
}
