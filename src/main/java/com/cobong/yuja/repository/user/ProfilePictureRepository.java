package com.cobong.yuja.repository.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cobong.yuja.model.ProfilePicture;

public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long>{
	Optional<ProfilePicture> findByUserUserId(Long userId);
	Optional<ProfilePicture> findByUserNickname(String nickname);
	@Query("SELECT P FROM ProfilePicture P WHERE flag = 0")
	List<ProfilePicture> findAllByFlag();
	
//	@Query("SELECT P FROM PofilePicture P WHERE P.userId = :userId AND flag = :1")
//	ProfilePicture findByUserIdAndFlag(@Param("userId") Long userId);
}
