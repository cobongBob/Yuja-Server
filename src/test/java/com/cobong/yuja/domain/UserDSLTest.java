package com.cobong.yuja.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cobong.yuja.repository.user.UserRepostitory;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class UserDSLTest {
	@Autowired
	private UserRepostitory userRepostitory;

	@Test
	public void userDsl_test() {

	}
}
