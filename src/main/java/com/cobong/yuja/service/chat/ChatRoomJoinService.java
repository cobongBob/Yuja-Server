package com.cobong.yuja.service.chat;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.ChatRoom;
import com.cobong.yuja.model.ChatRoomJoin;
import com.cobong.yuja.model.Notification;
import com.cobong.yuja.repository.NotificationRepository;
import com.cobong.yuja.repository.chat.ChatRoomJoinRepository;
import com.cobong.yuja.repository.chat.ChatRoomRepository;
import com.cobong.yuja.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomJoinService {
	private final ChatRoomJoinRepository chatRoomJoinRepository;
	private final UserRepository userRepository;
	private final NotificationRepository notificationRepository;
	private final ChatRoomRepository chatRoomRepository;
	
	@Transactional
	public void createRoomJoins(Long receiverId,Long senderId, ChatRoom chatRoom) {
		ChatRoomJoin receiverJoin = ChatRoomJoin.builder().chatRoom(chatRoom)
		.user(userRepository.findById(receiverId).orElseThrow(() -> new IllegalAccessError("채팅을 받을 유저가 존재하지 않습니다."))).build();
		
		ChatRoomJoin senderJoin = ChatRoomJoin.builder().chatRoom(chatRoom)
				.user(userRepository.findById(senderId).orElseThrow(() -> new IllegalAccessError("채팅을 받을 유저가 존재하지 않습니다."))).build();
		
		
		
//		// create notification
//		String type = "chatNoti"; 
//		Notification notification = new Notification().createNotification(
//				null, 
//				null, 
//				userRepository.findById(senderId).orElseThrow(() -> new IllegalAccessError("알림 보낸 유저 없음")), 
//				userRepository.findById(receiverId).orElseThrow(() -> new IllegalAccessError("알림 받는 유저 없음")),
//				type,
//				null);
//		notificationRepository.save(notification);
		
		
		
		chatRoomJoinRepository.save(receiverJoin);
		chatRoomJoinRepository.save(senderJoin);
	}

	@Transactional(readOnly = true)
	public Long checkForRoom(Long receiver, Long sender) {
		List<ChatRoomJoin> receiverList = chatRoomJoinRepository.findByUserUserId(receiver);
		List<ChatRoomJoin> senderList = chatRoomJoinRepository.findByUserUserId(sender);
		for(ChatRoomJoin joins: senderList) {
			if(receiverList.contains(joins)) {
				return joins.getRoomJoinId();
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
			throw new IllegalAccessError("방이 존재하지 않습니다.");
		}
		return receiver;
	}
	
}
