package com.cobong.yuja.repository.boardLiked;

public interface CustomBoardLikedRepository {
	String like(Long userId, Long boardId);
}