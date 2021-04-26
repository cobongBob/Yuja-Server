package com.cobong.yuja.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cobong.yuja.repository.UserRepository;
import com.cobong.yuja.web.UserControllerUnitTest;

import lombok.extern.slf4j.Slf4j;

/**
* 단위 테스트(service와 관련된 애들만 메모리에 띄움)
* service와 관련된놈이 repository밖에 없다.
*	UserRepository => 가짜 객체로만들수 있음
*/
@Slf4j
@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
	//UserService객체가 만들어질때 UserServiceUnitTest파일에 @Mock 으로 등록된 모든 애들을 주입받는다
		@InjectMocks
		private UserService userService;
		
		@Mock//Ioc가 아닌 따른 공간에 생성..
		private UserRepository userRepository;
}
