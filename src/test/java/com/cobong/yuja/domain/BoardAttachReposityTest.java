package com.cobong.yuja.domain;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cobong.yuja.model.BoardAttach;
import com.cobong.yuja.repository.attach.AttachRepository;
import com.cobong.yuja.repository.user.UserRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class BoardAttachReposityTest {
	@Autowired
	AttachRepository attachRepository;
	
	@Test
	public void findByfileNameTest() {
		List<BoardAttach> boardAttach = attachRepository.findAllByFlag();
		
		log.info(boardAttach);
	}
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void checkidAndNickName() {
		if(userRepository.existsByNickname("qwqwe")) {
			System.out.println("Success");
		} else {
			System.out.println("\n  Failed!  \n");
		}
	}
}
