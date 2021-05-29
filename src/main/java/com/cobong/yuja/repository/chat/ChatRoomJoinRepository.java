package com.cobong.yuja.repository.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cobong.yuja.model.ChatRoomJoin;

public interface ChatRoomJoinRepository extends JpaRepository<ChatRoomJoin, Long>{
	List<ChatRoomJoin> findByUserUserId(Long userid);
	
	List<ChatRoomJoin> findByChatRoomRoomId(Long roomId);

	@Query("SELECT j FROM ChatRoomJoin j WHERE roomId =:roomId AND userId =:userId")
	Optional<ChatRoomJoin> findByRoomIdAndUserId(@Param("roomId") Long roomId, @Param("userId") Long userId);
}
