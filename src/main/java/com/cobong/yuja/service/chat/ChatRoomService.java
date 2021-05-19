package com.cobong.yuja.service.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.ChatRoom;
import com.cobong.yuja.model.ChatRoomJoin;
import com.cobong.yuja.model.User;
import com.cobong.yuja.payload.request.chat.ChatRoomDto;
import com.cobong.yuja.repository.chat.ChatRoomJoinRepository;
import com.cobong.yuja.repository.chat.ChatRoomRepository;
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
			ChatRoom curRoom = join.getChatRoom();
			String receiver = chatRoomJoinService.findReceiver(curRoom.getRoomId(), userId);
			ChatRoomDto dto = new ChatRoomDto().create(curRoom.getRoomId(), receiver, "성공이다!!!!!");
			
			//if문 안에  curRoom.getMessages().get(curRoom.getMessages().size()-1).getMessage()
			dtoList.add(dto);
		}
		return dtoList;
	}	
}
