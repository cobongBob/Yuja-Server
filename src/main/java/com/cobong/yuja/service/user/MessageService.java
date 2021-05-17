package com.cobong.yuja.service.user;

import java.util.List;

import com.cobong.yuja.payload.request.user.MessageRequestDto;
import com.cobong.yuja.payload.response.user.MessageResponseDto;

public interface MessageService {
	
	MessageResponseDto send(MessageRequestDto dto);
	
	List<MessageResponseDto> findAll();
	
	MessageResponseDto findById(Long bno);
		
	String delete(Long bno);
}
