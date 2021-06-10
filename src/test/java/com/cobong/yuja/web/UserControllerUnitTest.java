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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.cobong.yuja.controller.UserApiController;
import com.cobong.yuja.model.User;
import com.cobong.yuja.payload.request.user.UserSaveRequestDto;
import com.cobong.yuja.payload.request.user.UserUpdateRequestDto;
import com.cobong.yuja.payload.response.user.UserResponseDto;
import com.cobong.yuja.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		User user = dto.dtoToEntity();
		
		UserResponseDto resUser = new UserResponseDto();
		String content = new ObjectMapper().writeValueAsString(user);

		//when
		when(userService.save(dto)).thenReturn(resUser);
		
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
		UserResponseDto reres = new UserResponseDto().entityToDto(resUser.dtoToEntity());
		
		userService.save(resUser);
		when(userService.findById(1L,90L)).thenReturn(reres);
		//when
		Long id = 1L;
		this.mockMvc.perform(get("/api/user/{userId}", id)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("테스트 제목1"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void getAll_test() throws Exception {
		//given
		UserSaveRequestDto resUser = new UserSaveRequestDto();
		resUser.setUsername("테스트 제목1");
		resUser.setPassword("테스트 내용1");
		resUser.setNickname("테스트 썸네일");
		resUser.setRealName("Real Name Test");
		resUser.setBday("1995-08-05");
		
		UserSaveRequestDto resUser1 = new UserSaveRequestDto();
		resUser.setUsername("테스트 제목11");
		resUser.setPassword("테스트 내용11");
		resUser.setNickname("테스트 썸네일1");
		resUser.setRealName("Real Name Test1");
		resUser.setBday("1995-08-051");
		
		UserSaveRequestDto resUser2 = new UserSaveRequestDto();
		resUser.setUsername("테스트 제목12");
		resUser.setPassword("테스트 내용12");
		resUser.setNickname("테스트 썸네일2");
		resUser.setRealName("Real Name Test2");
		resUser.setBday("1995-08-052");
		
		UserResponseDto reres = new UserResponseDto().entityToDto(resUser.dtoToEntity());
		UserResponseDto reres1 = new UserResponseDto().entityToDto(resUser1.dtoToEntity());
		UserResponseDto reres2 = new UserResponseDto().entityToDto(resUser2.dtoToEntity());
		
		List<UserResponseDto> testList = new ArrayList<UserResponseDto>();
		
		testList.add(reres);
		testList.add(reres1);
		testList.add(reres2);
		
		userService.save(resUser);
		userService.save(resUser1);
		userService.save(resUser2);
		
		//when
		when(userService.findAll()).thenReturn(testList);

		//then
		this.mockMvc.perform(get("/api/user")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void modify_test() throws Exception {
		//given
		User resUser = new User(0L,"테스트 제목1",null,null,"테스트 내용12","Real Name Test","1995-08-05",null,null,null,null,null,null,null, false, false, null, false, null, null, null, null, null);
		
		UserResponseDto resRes = new UserResponseDto().entityToDto(resUser);
		
		UserUpdateRequestDto testUser = new UserUpdateRequestDto();
		
		testUser.setPassword("테스트 내용12");
		
		String content = new ObjectMapper().writeValueAsString(testUser);
		
		when(userService.modify(1L, testUser, null)).thenReturn(resRes);
		
		//when
		Long id = 1L;
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
		//given
				
		when(userService.delete(1L, null)).thenReturn("success");
		
		//when
		Long id = 1L;
		ResultActions resultAction = this.mockMvc.perform(delete("/api/user/{bno}", id)
				.accept(MediaType.TEXT_PLAIN));
		
		MvcResult requestResult = resultAction.andReturn();
	    String result = requestResult.getResponse().getContentAsString();
		assertEquals("success", result);
	}
}
