package com.cobong.yuja.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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

import com.cobong.yuja.model.User;
import com.cobong.yuja.payload.request.User.UserSaveRequestDto;
import com.cobong.yuja.payload.request.User.UserUpdateRequestDto;
import com.cobong.yuja.payload.response.user.UserResponseDto;
import com.cobong.yuja.repository.user.UserRepository;
import com.cobong.yuja.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

/**
 * 통합 테스트 (모든 Bean들을 똑같이 IoC 올리고 테스트하는것)
 * @SpringBootTest(webEnvironment = WebEnvironment.MOCK) 실제 톰켓을 올리는게 아니라 다른톰켓을로 테스트
 * @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) 실제 톰켓에서에서 테스트 
 * @AutoConfigureMockMvc MockMvc를 Ioc에 등록해줌
 * @Transactional 은 각 각의 테스트함수가 종료 될때마다 트랜잭션을 rollback해주는 어노테이션
 */

@Log4j2
@Transactional
@AutoConfigureMockMvc // 단위테스트용 @WebMvcTest 에는 포함되어있다.
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) 
// 모든 애들이 메모리에 다 뜬다.
public class UserControllerIntegreTest {
	@Autowired
	private MockMvc mockMvc; //@@AutoConfigureMockMvc가 필요하다.
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach
	public void init() {
		entityManager.createNativeQuery("ALTER TABLE user AUTO_INCREMENT=1").executeUpdate();
		for(int i = 0; i<3; i++) {
			UserSaveRequestDto resUser = new UserSaveRequestDto();
			resUser.setUsername("테스트 제목"+i);
			resUser.setPassword("테스트 내용"+i);
			resUser.setNickname("테스트 썸네일"+i);
			resUser.setRealName("Real Name Test"+i);
			resUser.setBday("1995-08-0"+i);
			resUser.setUserIp("165,849,949,4"+i);
			userService.save(resUser);
		}
	}
	
	@Test
	public void save_test() throws Exception {
		//given
		UserSaveRequestDto dto =new UserSaveRequestDto();
		dto.setUsername("테스트 제목123");
		dto.setPassword("테스트 내용123");
		dto.setNickname("테스트 썸네일123");
		dto.setRealName("Real Name Test123");
		dto.setBday("1995-08-123");
		dto.setUserIp("165,849,949,123");
		User user = dto.dtoToEntity();
		
		String content = new ObjectMapper().writeValueAsString(user);

		//then
		this.mockMvc.perform(post("/api/user")
									.contentType(MediaType.APPLICATION_JSON)
									.content(content)
									.accept(MediaType.APPLICATION_JSON))
									.andExpect(status().isCreated())
									.andExpect(jsonPath("$.username").value("테스트 제목123"))
									.andExpect(jsonPath("$.password").value("테스트 내용123"))
									.andExpect(jsonPath("$.nickname").value("테스트 썸네일123"))
									.andExpect(jsonPath("$.realName").value("Real Name Test123"))
									.andExpect(jsonPath("$.bday").value("1995-08-123"))
									.andExpect(jsonPath("$.userIp").value("165,849,949,123"))
									.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void get_test() throws Exception {
		Long id = 1L;
		this.mockMvc.perform(get("/api/user/{userId}", id)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("테스트 제목0"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void getAll_test() throws Exception {
		this.mockMvc.perform(get("/api/user")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void modify_test() throws Exception {
		UserUpdateRequestDto testUser = new UserUpdateRequestDto();
		testUser.setPassword("테스트 내용12");
		
		Long id = 1L;
		String content = new ObjectMapper().writeValueAsString(testUser);
		
		this.mockMvc.perform(put("/api/user/{bno}", id)
									.contentType(MediaType.APPLICATION_JSON)
									.content(content)
									.accept(MediaType.APPLICATION_JSON))
									.andExpect(status().isOk())
									.andExpect(jsonPath("$.password").value("테스트 내용12"))
									.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void delete_test() throws Exception {
		Long id = 1L;
		ResultActions resultAction = this.mockMvc.perform(delete("/api/user/{bno}", id)
				.accept(MediaType.TEXT_PLAIN));
		
		MvcResult requestResult = resultAction.andReturn();
	    String result = requestResult.getResponse().getContentAsString();
		assertEquals("success", result);
	}
}
