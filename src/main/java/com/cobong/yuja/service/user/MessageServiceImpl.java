package com.cobong.yuja.service.user;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.cobong.yuja.model.Message;
import com.cobong.yuja.payload.request.user.MessageRequestDto;
import com.cobong.yuja.payload.response.user.MessageResponseDto;
import com.cobong.yuja.repository.user.MessageRepository;
import com.cobong.yuja.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
	private final UserRepository userRepository;
	private final MessageRepository messageRepository; 
	
	@Override
	@Transactional
	public MessageResponseDto send(MessageRequestDto dto) {
		Message message = Message.builder()
				.sender(userRepository.findById(dto.getSenderId())
						.orElseThrow(()-> new IllegalArgumentException("메세지 보낸 유저 없음"+dto.getSenderId())))
				.recipient(userRepository.findById(dto.getResipeintId())
						.orElseThrow(()-> new IllegalArgumentException("메세지 받은 유저 없음"+dto.getResipeintId())))
				.contents(dto.getContents())
				.build();
				
		messageRepository.save(message);
		return new MessageResponseDto().entityToDto(message);
	}

	@Override
	@Transactional
	public List<MessageResponseDto> findAll() {
		List<MessageResponseDto> messages = new ArrayList<MessageResponseDto>();
		List<Message> entityList = messageRepository.findAll();
		
		for(Message message : entityList) {
			MessageResponseDto dto = new MessageResponseDto().entityToDto(message);
			messages.add(dto);
		}
		return messages;
	}

	@Override
	@Transactional
	public MessageResponseDto findById(Long bno) {
		Message message =  messageRepository.findById(bno).orElseThrow(()-> new IllegalArgumentException("해당 메세지가 없습니다."));
		MessageResponseDto dto = new MessageResponseDto().entityToDto(message);
		return dto;
	}

	@Override
	@Transactional
	public String delete(Long bno) {
		Message message = messageRepository.findById(bno).orElseThrow(()-> new IllegalArgumentException("해당 메세지가 없습니다."));
		messageRepository.deleteById(bno);
		return "success";
	}

}
