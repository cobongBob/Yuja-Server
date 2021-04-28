package com.cobong.yuja.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cobong.yuja.model.User;

public interface UserRepostitory extends JpaRepository<User, Long> {
	

}
