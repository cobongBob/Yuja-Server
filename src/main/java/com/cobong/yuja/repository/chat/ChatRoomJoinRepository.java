package com.cobong.yuja.repository.chat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cobong.yuja.model.ChatRoomJoin;

public interface ChatRoomJoinRepository extends JpaRepository<ChatRoomJoin, Long>{
	List<ChatRoomJoin> findByUserUserId(Long userid);
	
	List<ChatRoomJoin> findByChatRoomRoomId(Long roomId);
}
