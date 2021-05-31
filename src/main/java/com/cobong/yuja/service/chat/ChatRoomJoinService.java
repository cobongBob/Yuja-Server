package com.cobong.yuja.service.chat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.ChatRoom;
import com.cobong.yuja.model.ChatRoomJoin;
import com.cobong.yuja.repository.chat.ChatRoomJoinRepository;
import com.cobong.yuja.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomJoinService {
	private final ChatRoomJoinRepository chatRoomJoinRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public void createRoomJoins(Long receiverId,Long senderId, ChatRoom chatRoom) {
		ChatRoomJoin receiverJoin = ChatRoomJoin.builder().chatRoom(chatRoom)
		.user(userRepository.findById(receiverId).orElseThrow(() -> new IllegalAccessError("채팅을 받을 유저가 존재하지 않습니다."))).build();
		
		ChatRoomJoin senderJoin = ChatRoomJoin.builder().chatRoom(chatRoom)
				.user(userRepository.findById(senderId).orElseThrow(() -> new IllegalAccessError("채팅을 받을 유저가 존재하지 않습니다."))).build();

		
		
		chatRoomJoinRepository.save(receiverJoin);
		chatRoomJoinRepository.save(senderJoin);
	}

	@Transactional(readOnly = true)
	public Long checkForRoom(Long receiver, Long sender) {
		List<ChatRoomJoin> receiverList = chatRoomJoinRepository.findByUserUserId(receiver);
		List<ChatRoom> receiverRooms = new ArrayList<ChatRoom>();
		
		for(ChatRoomJoin receiverJoin : receiverList) {
			receiverRooms.add(receiverJoin.getChatRoom());
		}
		List<ChatRoomJoin> senderList = chatRoomJoinRepository.findByUserUserId(sender);
		
		for(ChatRoomJoin joins: senderList) {
			if(receiverRooms.contains(joins.getChatRoom())) {
				return joins.getChatRoom().getRoomId();
			}
		}
		return 0L;
	}
	
	@Transactional(readOnly = true)
	public String findReceiver(Long chatRoomId, Long senderId) {
		List<ChatRoomJoin> joins = chatRoomJoinRepository.findByChatRoomRoomId(chatRoomId);
		String receiver = "";
		for(ChatRoomJoin join: joins) {
			if(join.getUser().getUserId() != senderId) {
				receiver += join.getUser().getNickname();
			}
		}
		if(receiver.equals("")) {
			receiver += "방을 나간 유저입니다.";
		}
		return receiver;
	}
	
}
