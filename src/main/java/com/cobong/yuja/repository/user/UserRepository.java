package com.cobong.yuja.repository.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cobong.yuja.model.User;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
	Optional<User> findByUsername(String username);
	
	Boolean existsByNickname(String nickname);

	Boolean existsByUsername(String username);
	
	// banned 1 userIp list 가져오기
	@Query("SELECT u.userIp FROM User u WHERE u.banned = 1")
	List<String> findAllByBanned();
}
