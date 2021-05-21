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
import com.cobong.yuja.repository.user.ProfilePictureRepository;
import com.cobong.yuja.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
	private final ChatRoomRepository chatRoomRepository;
	private final ChatRoomJoinService chatRoomJoinService;
	private final ChatRoomJoinRepository chatroomjoinrepository;
	private final UserRepository userRepository;
	private final ProfilePictureRepository profileRepository;
	
	@Transactional
	public Long newRoom(String receiver, Long sender) {
		Optional<User> receiverEntity = userRepository.findByNickname(receiver);
		if(!receiverEntity.isPresent()) {
			throw new IllegalAccessError("해당 닉네임을 가진 유저가 없거나 잘못된 닉네임 입니다.");
		}
		if(receiverEntity.get().getUserId() == sender) {
			throw new IllegalAccessError("자기자신과는 채팅할수 없습니다.");
		}
		if(sender == 0L) {
			throw new IllegalAccessError("로그인해 주시기 바랍니다.");
		}
		
		Long roomId = chatRoomJoinService.checkForRoom(receiverEntity.get().getUserId(), sender);
		if(roomId != 0L) {
			return roomId;
		}
		ChatRoom save = new ChatRoom();
		ChatRoom chatRoom = chatRoomRepository.save(save);
		
		chatRoomJoinService.createRoomJoins(receiverEntity.get().getUserId(),sender, chatRoom);
		
		return chatRoom.getRoomId();
	}
	@Transactional(readOnly = true)
	public List<ChatRoomDto> findRooms(Long userId) {
		List<ChatRoomDto> dtoList = new ArrayList<ChatRoomDto>();
		List<ChatRoomJoin> joins = chatroomjoinrepository.findByUserUserId(userId);
		
		for(ChatRoomJoin join: joins) {
			ChatRoom curRoom = join.getChatRoom();
			String receiver = chatRoomJoinService.findReceiver(curRoom.getRoomId(), userId);			
			ChatRoomDto dto = new ChatRoomDto().create(curRoom.getRoomId(), receiver, "성공이다!!!!!");
			
			if(profileRepository.findByUserNickname(receiver).isPresent()) {
				dto.setProfilePic(profileRepository.findByUserNickname(receiver).get().getFileName());
			} 
			dtoList.add(dto);
		}
		return dtoList;
	}

	@Transactional
	public void deleteEmptyRooms() {
		List<ChatRoom> rooms = chatRoomRepository.findAll();
		
		for(ChatRoom room : rooms) {
			if(room.getMessages().size() == 0) {
				List<ChatRoomJoin> joins = chatroomjoinrepository.findByChatRoomRoomId(room.getRoomId());
				for(ChatRoomJoin join: joins) {
					chatroomjoinrepository.delete(join);
				}
				chatRoomRepository.delete(room);
			}
		}
	}
	public void delete(Long roomId) {
		chatRoomRepository.deleteById(roomId);
	}	
}
