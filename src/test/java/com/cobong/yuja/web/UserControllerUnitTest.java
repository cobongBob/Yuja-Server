package com.cobong.yuja.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cobong.yuja.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebMvcTest //controller관련된 빈만 뜬다.
public class UserControllerUnitTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean // Ioc환경에 bean 등록됨
	private UserService userService;
	
}
