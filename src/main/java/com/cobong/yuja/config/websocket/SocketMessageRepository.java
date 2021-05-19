package com.cobong.yuja.config.websocket;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SocketMessageRepository extends JpaRepository<SocketMessage, Long>{

}
