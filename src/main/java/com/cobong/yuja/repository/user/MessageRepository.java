package com.cobong.yuja.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cobong.yuja.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
