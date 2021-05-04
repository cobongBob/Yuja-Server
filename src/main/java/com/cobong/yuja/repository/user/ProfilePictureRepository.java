package com.cobong.yuja.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cobong.yuja.model.ProfilePicture;

public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long>{
	@Query("SELECT P FROM ProfilePicture P WHERER userId = :userId and flag = true")
	ProfilePicture findByUserIdAndFlag(@Param("userId") Long userId);
}
