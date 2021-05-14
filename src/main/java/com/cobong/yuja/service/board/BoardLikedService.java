package com.cobong.yuja.service.board;

public interface BoardLikedService {
	String liked(Long bno, Long userId);
	String disLiked(Long bno, Long userId);
}
