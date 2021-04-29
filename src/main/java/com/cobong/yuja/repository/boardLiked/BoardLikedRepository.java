package com.cobong.yuja.repository.boardLiked;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cobong.yuja.model.BoardLiked;

public interface BoardLikedRepository extends JpaRepository<BoardLiked, Long>, CustomBoardLikedRepository{
	void deleteByUseridAndBoardid(Long user, Long boardid);
}