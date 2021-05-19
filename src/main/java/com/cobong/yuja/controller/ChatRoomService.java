package com.cobong.yuja.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.config.websocket.ChatRoom;
import com.cobong.yuja.config.websocket.ChatRoomDto;
import com.cobong.yuja.config.websocket.ChatRoomJoin;
import com.cobong.yuja.config.websocket.ChatRoomJoinRepository;
import com.cobong.yuja.config.websocket.ChatRoomJoinService;
import com.cobong.yuja.config.websocket.SocketMessageReceiveDto;
import com.cobong.yuja.config.websocket.SocketMessageSendDto;
import com.cobong.yuja.model.User;
import com.cobong.yuja.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
	private final ChatRoomRepository chatRoomRepository;
	private final ChatRoomJoinService chatRoomJoinService;
	private final ChatRoomJoinRepository chatroomjoinrepository;
	
	@Transactional
	public Long newRoom(Long receiver, Long sender) {
		if(receiver == sender) {
			throw new IllegalAccessError("자기자신과는 채팅할수 없습니다.");
		}
		Long roomId = chatRoomJoinService.checkForRoom(receiver, sender);
		if(roomId != 0L) {
			return roomId;
		}
		ChatRoom save = new ChatRoom();
		ChatRoom chatRoom = chatRoomRepository.save(save);
		
		chatRoomJoinService.createRoomJoins(receiver,sender, chatRoom);
		
		return chatRoom.getRoomId();
	}
	
	public List<ChatRoomDto> findRooms(Long userId) {
		List<ChatRoomDto> dtoList = new ArrayList<ChatRoomDto>();
		List<ChatRoomJoin> joins = chatroomjoinrepository.findByUserUserId(userId);
		
		for(ChatRoomJoin join: joins) {
			System.out.println("/////////////////////// join //////////////////////////");
			System.out.println(join.getChatRoom().getRoomId()+"   "+ join.getUser().getNickname());
			System.out.println("/////////////////////////////////////////////////");
			
			ChatRoom curRoom = join.getChatRoom();
			String receiver = chatRoomJoinService.findReceiver(curRoom.getRoomId(), userId);
			ChatRoomDto dto = new ChatRoomDto().create(curRoom.getRoomId(), receiver, "성공이다 씨바아아아아아아아알");
			
				//curRoom.getMessages().get(curRoom.getMessages().size()-1).getMessage()
			dtoList.add(dto);
		}
		return dtoList;
	}	
}
