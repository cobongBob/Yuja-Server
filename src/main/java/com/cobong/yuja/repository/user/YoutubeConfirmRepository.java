package com.cobong.yuja.repository.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cobong.yuja.model.YoutubeConfirm;

public interface YoutubeConfirmRepository extends JpaRepository<YoutubeConfirm, Long>{
	Optional<YoutubeConfirm> findByUserUserId(Long userId);
	
	@Query("SELECT Y FROM YoutubeConfirm Y WHERE flag = 0")
	List<YoutubeConfirm> findAllByFlag();
	
	@Query("SELECT Y FROM YoutubeConfirm Y WHERE authorized = 0")
	List<YoutubeConfirm> findAllByAuthorized();
}
