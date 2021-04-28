package com.cobong.yuja.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cobong.yuja.repository.user.UserRepository;
import com.cobong.yuja.service.UserService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class UserDSLTest {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;

}
