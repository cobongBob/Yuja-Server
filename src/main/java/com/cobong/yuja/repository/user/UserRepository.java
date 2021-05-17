package com.cobong.yuja.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cobong.yuja.model.User;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
	Optional<User> findByUsername(String username);
	
	Boolean existsByNickname(String nickname);

	Boolean existsByUsername(String username);
}
