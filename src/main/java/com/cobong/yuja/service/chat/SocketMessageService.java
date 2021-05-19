package com.cobong.yuja.service.chat;

import java.nio.channels.IllegalChannelGroupException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cobong.yuja.model.ChatRoom;
import com.cobong.yuja.model.SocketMessage;
import com.cobong.yuja.model.User;
import com.cobong.yuja.payload.request.chat.SocketMessageReceiveDto;
import com.cobong.yuja.payload.response.chat.SocketMessageSendDto;
import com.cobong.yuja.repository.chat.ChatRoomRepository;
import com.cobong.yuja.repository.chat.SocketMessageRepository;
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
}
