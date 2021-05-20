package com.cobong.yuja.repository.chat;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cobong.yuja.model.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>{
	Optional<ChatRoom> findByRoomId(Long roomId);
}
