package com.cobong.yuja.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cobong.yuja.model.Thumbnail;
import com.cobong.yuja.repository.attach.ThumbnailRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class ThumbnailRepositoryTest {
	@Autowired
	private ThumbnailRepository tr;
	
	@Test
	public void test() {
		Thumbnail t = tr.findByBoardBoardId(1L);
		log.info(t);
	}
}
