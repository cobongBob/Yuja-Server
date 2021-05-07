package com.cobong.yuja.repository.attach;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cobong.yuja.model.Thumbnail;

public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long>{
	Optional<Thumbnail> findByBoardBoardId(Long boardId);
}
