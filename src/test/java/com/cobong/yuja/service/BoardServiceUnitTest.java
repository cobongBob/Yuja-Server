package com.cobong.yuja.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cobong.yuja.repository.BoardRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@ExtendWith(MockitoExtension.class)
public class BoardServiceUnitTest {
	@InjectMocks
	private BoardService boardService;
	
	@Mock
	private BoardRepository boardRepository;
}
