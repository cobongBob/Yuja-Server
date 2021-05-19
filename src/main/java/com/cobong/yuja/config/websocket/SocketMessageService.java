package com.cobong.yuja.config.websocket;

import java.nio.channels.IllegalChannelGroupException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.controller.ChatRoomRepository;
import com.cobong.yuja.model.User;
import com.cobong.yuja.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SocketMessageService {
	private final SocketMessageRepository socketMessageRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public void save(SocketMessageReceiveDto msg) {
		ChatRoom chatRoom = chatRoomRepository.findById(msg.getChatRoomId()).orElseThrow(() -> new IllegalAccessError("채팅방이 존재하지 않습니다"));
		Optional<User> userOpt = userRepository.findByNickname(msg.getSender());
		if(!userOpt.isPresent()) {
			throw new IllegalAccessError("채팅을 연결하려는 해당 유저가 존재하지 않습니다");
		}
		User user = userOpt.get();
		SocketMessage socketmsg = SocketMessage.builder().user(user).chatRoom(chatRoom).message(msg.getMessage()).build();
		
		socketMessageRepository.save(socketmsg);
	}
	
	@Transactional
	public List<SocketMessageSendDto> getAllMsgs(Long chatRoomId){
		if(!chatRoomRepository.findByRoomId(chatRoomId).isPresent()) {
			throw new IllegalAccessError("접근하려는 채팅방이 존재하지 않습니다.");
		}
		
		ChatRoom curRoom = chatRoomRepository.findByRoomId(chatRoomId).get();
		List<SocketMessage> entityList = curRoom.getMessages();
		Collections.reverse(entityList);
		
		List<SocketMessageSendDto> dtoList = new ArrayList<SocketMessageSendDto>();
		for(SocketMessage msgs : entityList) {
			SocketMessageSendDto dto = new SocketMessageSendDto().entityToDto(msgs);
			dtoList.add(dto);
		}
		
		return dtoList;
	}
}
