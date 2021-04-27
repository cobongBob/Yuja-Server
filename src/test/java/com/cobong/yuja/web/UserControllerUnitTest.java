package com.cobong.yuja.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.cobong.yuja.controller.UserApiController;
import com.cobong.yuja.model.User;
import com.cobong.yuja.payload.request.UserSaveRequestDto;
import com.cobong.yuja.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@WebMvcTest(UserApiController.class) //controller관련된 빈만 뜬다.
@MockBean(JpaMetamodelMappingContext.class)
public class UserControllerUnitTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean // Ioc환경에 bean 등록됨
	private UserService userService;
	
	@Test
	public void save_test() throws Exception {
		//given
		UserSaveRequestDto dto =new UserSaveRequestDto();
		dto.setUsername("테스트 제목1");
		dto.setPassword("테스트 내용1");
		dto.setNickname("테스트 썸네일");
		dto.setRealName("Real Name Test");
		dto.setBday("1995-08-05");
		dto.setUserIp("165,849,949,466");
		User user = dto.dtoToEntity();
		
		User resUser = new User();
		resUser.setUsername("테스트 제목1");
		resUser.setPassword("테스트 내용1");
		resUser.setNickname("테스트 썸네일");
		resUser.setRealName("Real Name Test");
		resUser.setBday("1995-08-05");
		resUser.setUserIp("165,849,949,466");
		String content = new ObjectMapper().writeValueAsString(user);

		//when
		when(userService.userSave(dto)).thenReturn(resUser);
		
		//then
		this.mockMvc.perform(post("/api/user")
										.contentType(MediaType.APPLICATION_JSON)
										.content(content)
										.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.username").value("테스트 제목1"))
				.andExpect(jsonPath("$.password").value("테스트 내용1"))
				.andExpect(jsonPath("$.nickname").value("테스트 썸네일"))
				.andExpect(jsonPath("$.realName").value("Real Name Test"))
				.andExpect(jsonPath("$.bday").value("1995-08-05"))
				.andExpect(jsonPath("$.userIp").value("165,849,949,466"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void get_test() throws Exception {
		//given
		UserSaveRequestDto resUser = new UserSaveRequestDto();
		resUser.setUsername("테스트 제목1");
		resUser.setPassword("테스트 내용1");
		resUser.setNickname("테스트 썸네일");
		resUser.setRealName("Real Name Test");
		resUser.setBday("1995-08-05");
		resUser.setUserIp("165,849,949,466");
		
		userService.userSave(resUser);
		
		//when
		Long id = 1L;
		
		this.mockMvc.perform(get("/api/user/get/{userId}", id)
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
}
