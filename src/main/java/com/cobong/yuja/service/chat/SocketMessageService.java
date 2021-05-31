package com.cobong.yuja.service.chat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.ChatRoom;
import com.cobong.yuja.model.ChatRoomJoin;
import com.cobong.yuja.model.Notification;
import com.cobong.yuja.model.SocketMessage;
import com.cobong.yuja.model.User;
import com.cobong.yuja.payload.request.chat.SocketMessageReceiveDto;
import com.cobong.yuja.payload.response.chat.SocketMessageSendDto;
import com.cobong.yuja.repository.chat.ChatRoomJoinRepository;
import com.cobong.yuja.repository.chat.ChatRoomRepository;
import com.cobong.yuja.repository.chat.SocketMessageRepository;
import com.cobong.yuja.repository.notification.NotificationRepository;
import com.cobong.yuja.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SocketMessageService {
	private final SocketMessageRepository socketMessageRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final UserRepository userRepository;
	private final NotificationRepository notificationRepository;
	private final ChatRoomJoinRepository chatRoomJoinRepository;
	
	@Transactional
	public void save(SocketMessageReceiveDto msg) {
		ChatRoom chatRoom = chatRoomRepository.findById(msg.getChatRoomId()).orElseThrow(() -> new IllegalAccessError("채팅방이 존재하지 않습니다"));
		Optional<User> userOpt = userRepository.findByNickname(msg.getSender());
		if(!userOpt.isPresent()) {
			throw new IllegalAccessError("채팅을 연결하려는 해당 유저가 존재하지 않습니다");
		}
		User user = userOpt.get();
		SocketMessage socketmsg = SocketMessage.builder().user(user).chatRoom(chatRoom).message(msg.getMessage()).build();
		
		User sender = userRepository.findByNickname(msg.getSender()).orElseThrow(() -> new IllegalAccessError("알림 보낸 유저 없음 "+msg.getSender()));
		User receiver = userRepository.findByNickname(msg.getReceiver()).orElseThrow(() -> new IllegalAccessError("알림 보낼 유저 없음 "+msg.getReceiver()));
		
		Optional<ChatRoomJoin> join = chatRoomJoinRepository.findByRoomIdAndUserId(msg.getChatRoomId(), receiver.getUserId());
		if(join.isPresent()) {
			if(join.get().isDeleted()) {
				join.get().setDeleted(false);
			}
		}
		
		String type = "chatNoti"; 
		Optional<Long> lastNoti = notificationRepository.findByLastNoti(sender.getUserId(), receiver.getUserId(),type);
		Optional<Long> lastreceiveNoti = notificationRepository.findByLastNoti(receiver.getUserId(),sender.getUserId(),type);
		if(lastreceiveNoti.isPresent()) {
			//실시간 알림 제거
			notificationRepository.deleteById(lastreceiveNoti.get());
		}
		if(lastNoti.isPresent()) {
			//중복 알림 제거
			notificationRepository.deleteById(lastNoti.get());
		}
		Notification notification = new Notification().createNotification(
				null, 
				sender,
				receiver,
				type,
				null);
		notificationRepository.save(notification);
		
		socketMessageRepository.save(socketmsg);
	}
	
	@Transactional
	public List<SocketMessageSendDto> getAllMsgs(Long chatRoomId, Long userId){
		if(!chatRoomRepository.findByRoomId(chatRoomId).isPresent()) {
			throw new IllegalAccessError("접근하려는 채팅방이 존재하지 않습니다.");
		}
		
		ChatRoom curRoom = chatRoomRepository.findByRoomId(chatRoomId).get();
		List<SocketMessage> entityList = curRoom.getMessages();
		
		LocalDateTime now = LocalDateTime.now();
		
		List<SocketMessageSendDto> dtoList = new ArrayList<SocketMessageSendDto>();
		for(SocketMessage msgs : entityList) {
			SocketMessageSendDto dto = new SocketMessageSendDto().entityToDto(msgs);
			dto.setContent(dto.getContent().replaceAll("&lt", "<"));
			dto.setContent(dto.getContent().replaceAll("&gt", ">"));
			dto.setContent(dto.getContent().replaceAll("&quot", "\""));
			dto.setContent(dto.getContent().replaceAll("&#39", "\'"));
			dto.setContent(dto.getContent().replaceAll("&amp", "&"));
			LocalDateTime msgTime = LocalDateTime.ofInstant(msgs.getCreatedDate(), ZoneId.systemDefault());
			
			if(msgTime.getDayOfYear() != now.getDayOfYear()) {
				if(msgTime.getHour() > 12) {
					dto.setCreatedDate(msgTime.getYear()+"-"+msgTime.getMonthValue()+"-"+msgTime.getDayOfMonth()+"\n오후 "+(msgTime.getHour()%12)+" : "+msgTime.getMinute());					
				} else if(msgTime.getHour() == 12) {
					dto.setCreatedDate(msgTime.getYear()+"-"+msgTime.getMonthValue()+"-"+msgTime.getDayOfMonth()+"\n오후 "+(msgTime.getHour())+" : "+msgTime.getMinute());
				} else {
					dto.setCreatedDate(msgTime.getYear()+"-"+msgTime.getMonthValue()+"-"+msgTime.getDayOfMonth()+"\n오전 "+msgTime.getHour()+" : "+msgTime.getMinute());
				}
			} else {
				if(msgTime.getHour() > 12) {
					dto.setCreatedDate("오후 "+msgTime.getHour()%12+" : "+msgTime.getMinute());					
				}else if(msgTime.getHour() == 12){
					dto.setCreatedDate("오후 "+msgTime.getHour()+" : "+msgTime.getMinute());
				}else {
					dto.setCreatedDate("오전 "+msgTime.getHour()+" : "+msgTime.getMinute());
				}
			}
			
			if(msgs.getUser().getUserId() != userId) {
				dto.setOwnerOrNot(false);
			} else {
				dto.setOwnerOrNot(true);
			}
			dtoList.add(dto);
		}
		return dtoList;
	}

	@Transactional
	public void delete2weeksOld() {
		List<SocketMessage> msgList = socketMessageRepository.findAll();
		LocalDateTime now = LocalDateTime.now();
		
		for(SocketMessage msg: msgList) {
			LocalDateTime msgTime = LocalDateTime.ofInstant(msg.getCreatedDate(), ZoneId.systemDefault());
			if(now.getDayOfYear()-msgTime.getDayOfYear()>14) {
				socketMessageRepository.delete(msg);
			}
		}
	}
}
