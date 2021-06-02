package com.cobong.yuja.domain;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cobong.yuja.model.User;
import com.cobong.yuja.repository.user.UserRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class UserDSLTest {

	@Autowired
	private UserRepository userRepository;

//	@Autowired
//	private EntityManager entityManager;


//	private User settingUser() {
//		User settingUser = User.builder()
//				.username("user1")
//				.userId(1L)
//				.build();
//
//		return userRepository.save(settingUser);
//	}
	
	@Test
	public void save_test2() {
		List<User> list = userRepository.findByEmail("user1");
		for(User user : list) {
			log.info(user);
		}
	}

}
