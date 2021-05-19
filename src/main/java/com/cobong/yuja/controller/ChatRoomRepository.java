package com.cobong.yuja.controller;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cobong.yuja.config.websocket.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>{
	Optional<ChatRoom> findByRoomId(Long roomId);
}
