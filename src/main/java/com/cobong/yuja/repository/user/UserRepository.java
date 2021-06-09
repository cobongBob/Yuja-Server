package com.cobong.yuja.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cobong.yuja.model.User;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
	Optional<User> findByUsername(String username);
	
	Boolean existsByNickname(String nickname);

	Boolean existsByUsername(String username);
	
	Optional<User> findByNickname(String nickname);
	
	@Query("SELECT u.userId FROM User u WHERE u.nickname =:nickname")
	Optional<Long> findIdByNickname(@Param("nickname") String nickname);
}
