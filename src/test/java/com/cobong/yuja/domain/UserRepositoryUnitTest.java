package com.cobong.yuja.domain;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cobong.yuja.repository.user.UserRepository;

import lombok.extern.log4j.Log4j2;

//단위 테스트( DB 관련된 Bean이 Ioc에 등록되면 됨)
@Log4j2
@AutoConfigureTestDatabase(replace = Replace.NONE) //가짜 db로 테스트, replace.none은 실제 db로 테스트
@DataJpaTest //jpa관련 애들만 뜬다. repository들을 다 ioc에 등록해줌.
public class UserRepositoryUnitTest {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired EntityManager entityManager;
	
	@BeforeEach
	public void init() {
		entityManager.createNativeQuery("ALTER TABLE user AUTO_INCREMENT=1").executeUpdate();
	}
	
	@Test
	public void testbannedall() {
	List<String> bannedList = userRepository.findAllByBanned();
	log.info(bannedList);
	}
}
