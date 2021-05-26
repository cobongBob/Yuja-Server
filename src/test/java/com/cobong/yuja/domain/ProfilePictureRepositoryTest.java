package com.cobong.yuja.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cobong.yuja.repository.attach.ProfilePictureRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class ProfilePictureRepositoryTest {
	@Autowired
	private ProfilePictureRepository ppr;
	
	@Test
	public void pprTest() {
		if(ppr.findByUserUserId(5L) != null) {
			System.out.println("NOT NULL!!!!!!!!!!!!!");
		} else {
			System.out.println("Null!!!");
		}
	}
}
