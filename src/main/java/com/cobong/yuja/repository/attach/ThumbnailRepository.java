package com.cobong.yuja.repository.attach;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cobong.yuja.model.Thumbnail;

public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long>{
	Optional<Thumbnail> findByBoardBoardId(Long boardId);
	
	@Query("SELECT T FROM Thumbnail T WHERE flag = 0")
	List<Thumbnail> findAllByFlag();
}
