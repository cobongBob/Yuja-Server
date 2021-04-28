package com.cobong.yuja.domain;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cobong.yuja.exception.ResourceNotFoundException;
import com.cobong.yuja.model.User;
import com.cobong.yuja.repository.user.CustomUserRepositoryImpl;
import com.cobong.yuja.repository.user.UserRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class UserDSLTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EntityManager entityManager;


	private User settingUser() {
		User settingUser = User.builder()
				.username("user1")
				.userId(1L)
				.build();

		return userRepository.save(settingUser);
	}

}
