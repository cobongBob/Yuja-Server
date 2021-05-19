package com.cobong.yuja.config.websocket;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>{
	Optional<ChatRoom> findByRoomId(Long roomId);
}
