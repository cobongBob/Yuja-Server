package com.cobong.yuja.repository.liked;

public interface CustomBoardLikedRepository {
	String like(Long userId, Long boardId);
}