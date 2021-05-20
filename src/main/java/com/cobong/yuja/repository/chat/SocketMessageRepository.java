package com.cobong.yuja.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cobong.yuja.model.SocketMessage;

public interface SocketMessageRepository extends JpaRepository<SocketMessage, Long>{

}
