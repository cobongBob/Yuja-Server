package com.cobong.yuja.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cobong.yuja.model.ProfilePicture;

public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long>{
	ProfilePicture findByUserUserId(Long userId);
	
//	@Query("SELECT P FROM PofilePicture P WHERE P.userId = :userId AND flag = :1")
//	ProfilePicture findByUserIdAndFlag(@Param("userId") Long userId);
}
