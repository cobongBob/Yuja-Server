package com.cobong.yuja.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.Board;
import com.cobong.yuja.payload.request.BoardSaveRequestDto;
import com.cobong.yuja.repository.BoardRepository;
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
	private EntityManager entityManager;
	
	@BeforeEach
	public void init() {
		entityManager.createNativeQuery("ALTER TABLE user AUTO_INCREMENT=1").executeUpdate();
	}
	
	@Test
	public void save_test() throws Exception {
		//given
		BoardSaveRequestDto dto =new BoardSaveRequestDto();
		dto.setTitle("테스트 제목1");
		dto.setContent("테스트 내용1");
		dto.setThumbnail("테스트 썸네일");
		Board board = dto.dtoToEntity();
		String content = new ObjectMapper().writeValueAsString(board);
		
		ResultActions resultActions = mockMvc.perform(post("/api/board")
										.contentType(MediaType.APPLICATION_JSON_UTF8)
										.content(content)
										.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultActions
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.title").value("테스트 제목1"))
				.andExpect(jsonPath("$.content").value("테스트 내용1"))
				.andExpect(jsonPath("$.thumbnail").value("테스트 썸네일"))
				.andDo(MockMvcResultHandlers.print());
	}

}
