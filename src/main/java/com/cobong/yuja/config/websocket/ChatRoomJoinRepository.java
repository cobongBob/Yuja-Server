package com.cobong.yuja.config.websocket;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomJoinRepository extends JpaRepository<ChatRoomJoin, Long>{
	List<ChatRoomJoin> findByUserUserId(Long userid);
	
	List<ChatRoomJoin> findByChatRoomRoomId(Long roomId);
}
